package com.example.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.User;
import com.example.entity.dto.UserDetails;
import com.example.entity.vo.request.auth.ConfirmResetReq;
import com.example.entity.vo.request.auth.UserLoginReq;
import com.example.entity.vo.request.auth.UserRegisterReq;
import com.example.entity.vo.request.user.ChangePasswordReq;
import com.example.entity.vo.request.user.EmailResetReq;
import com.example.entity.vo.request.user.ModifyEmailReq;
import com.example.entity.vo.response.auth.UserLoginResp;
import com.example.exception.auth.UsernameOrEmailExistsException;
import com.example.repository.user.UserDetailsRepository;
import com.example.repository.user.UserRepository;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import com.example.utils.JwtUtils;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    //验证邮件发送冷却时间限制，秒为单位
    @Value("${spring.web.verify.mail-limit}")
    int verifyLimit;

    @Resource
    private UserRepository userRepository;
    @Resource
    private Converter converter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private AmqpTemplate rabbitTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FlowUtils flow;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserDetailsRepository userDetailsRepository;


    public UserLoginResp login(UserLoginReq req) throws Exception {
        User user = userRepository.findByUsernameOrEmail(req.getText());
        if (user == null) {
            throw new Exception("用户不存在");
        }
        if (!BCrypt.checkpw(req.getPassword(), user.getPassword())) {
            throw new Exception("用户名或密码错误");
        }
        StpUtil.login(user.getId());
        UserLoginResp resp = converter.convert(user, UserLoginResp.class);
        resp.setToken(
                StpUtil.getTokenValue()
        ).setExpire(
                LocalDateTime.now().plusSeconds(StpUtil.getTokenTimeout())
        ).setRoles(List.of()).setPermissions(List.of());
        return resp;
    }

    //注册邮箱验证（发送验证码）
    public void registerEmailVerify(String type, String email, String address) throws Exception {
        synchronized (address.intern()) {
            if (!this.verifyLimit(address))
                throw new Exception("请求频繁，请稍后再试");
            //创建一个 Random 对象，并生成一个六位随机验证码。验证码的范围是从 100000 到 999999。
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            //使用 Map.of 创建一个不可变的 Map，其中包含了要发送的数据，包括 type、email 和生成的 code。
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            rabbitTemplate.convertAndSend(Const.MQ_MAIL, data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
        }
    }


    public void register(UserRegisterReq req) throws Exception {
        // 判断用户名或者邮箱是否存在
        if (userRepository.usernameExists(req.getUsername()) ||
                userRepository.emailExists(req.getEmail())
        ) {
            throw new UsernameOrEmailExistsException();
        }
        //验证码校验
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + req.getEmail());
        if (code == null) {
            throw new Exception("验证码不能为空");
        }
        if (!code.equals(req.getCode())) {
            throw new Exception("验证码错误");
        }

        User user = converter.convert(req, User.class);
        // 密码加密
        user.setPassword(BCrypt.hashpw(req.getPassword()));
        UserDetails userDetails = new UserDetails();
        transactionTemplate.execute(
                status -> {
                    userRepository.save(user);
                    userDetails.setUserId(user.getId());
                    userDetailsRepository.save(userDetails);
                    return null;
                }
        );
    }

    /**
     * 针对IP地址进行邮件验证码获取限流
     *
     * @param address 地址
     * @return 是否通过验证
     */
    private boolean verifyLimit(String address) {
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        return flow.limitOnceCheck(key, verifyLimit);
    }


    public void logout() {
        String token = StpUtil.getTokenValue();
        if (jwtUtils.invalidateJwt(token)) {
            StpUtil.logout();
        }
    }


    public String resetConfirm(ConfirmResetReq req) throws Exception {
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + req.getEmail());
        if (code == null) {
            throw new Exception("验证码不能为空");
        }
        if (!code.equals(req.getCode())) {
            throw new Exception("验证码错误");
        }
        return null;
    }

    public void resetEmailAccountPassword(EmailResetReq req) throws Exception {
        String email = req.getEmail();
        ConfirmResetReq confirmReq = converter.convert(req, ConfirmResetReq.class);
        String verify = this.resetConfirm(confirmReq);

        if (verify != null) {
            return;
        }
        String password = BCrypt.hashpw(req.getPassword());
        boolean update = userRepository.lambdaUpdate().eq(User::getEmail, email).set(User::getPassword, password).update();
        //若更新密码成功，则删除redis中的旧数据（验证码）
        if (update) stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
    }


    public String modifyEmail(ModifyEmailReq req) throws Exception {
        String email = req.getEmail();
        String code = this.getEmailVerifyCode(email);
        //QxkQuestion25.1.5:id和userId有点搞混了，没理清楚关系
        Long id = StpUtil.getLoginIdAsLong();

        if (code == null) return "请先获取验证码！";
        if (!code.equals(req.getCode())) return "验证码错误，请重新输入";
        this.deleteEmailVerifyCode(email);

        User user = userRepository.findByUsernameOrEmail(email);

        if (user != null && !Objects.equals(user.getId(), id)) {
            return "该电子邮件已经被其他账号绑定，无法完成此操作";
        }
        userRepository.lambdaUpdate().set(User::getEmail, email).eq(User::getId, id).update();
        return null;
    }


    public void changePassword(ChangePasswordReq req) throws Exception {
        Long id = StpUtil.getLoginIdAsLong();
        //判断当前输入的原密码是否和当前用户密码相等
        String userPassword = userRepository.lambdaQuery().eq(User::getId, id).one().getPassword();
        if (!BCrypt.checkpw(req.getPassword(), userPassword))
            throw new Exception("原密码错误，请重新输入");
        userRepository.lambdaUpdate().eq(User::getId, id).set(User::getPassword, BCrypt.hashpw(req.getNewPassword())).update();
    }




    /**
     * 移除Redis中存储的邮件验证码
     *
     * @param email 电邮
     */
    private void deleteEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取Redis中存储的邮件验证码
     *
     * @param email 电邮
     * @return 验证码
     */
    private String getEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        return stringRedisTemplate.opsForValue().get(key);
    }


}
