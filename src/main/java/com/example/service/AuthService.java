package com.example.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.User;
import com.example.entity.enums.ErrorCode;
import com.example.entity.vo.request.ConfirmResetReq;
import com.example.entity.vo.request.EmailResetReq;
import com.example.entity.vo.request.UserLoginReq;
import com.example.entity.vo.request.UserRegisterReq;
import com.example.entity.vo.response.UserLoginResp;
import com.example.exception.auth.UsernameOrEmailExistsException;
import com.example.repository.UserRepository;
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
            throw new UsernameOrEmailExistsException(ErrorCode.USERNAME_OR_EMAIL_EXISTS);
        }
        //验证码校验
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + req.getEmail());
        if (code == null) {throw new Exception("验证码不能为空");}
        if (!code.equals(req.getCode())){throw new Exception("验证码错误");}

        User user = converter.convert(req, User.class);
        // 密码加密
        user.setPassword(BCrypt.hashpw(req.getPassword()));
        transactionTemplate.execute(
                status -> userRepository.save(user)
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


    public void logout(){
        String token = StpUtil.getTokenValue();
        if (jwtUtils.invalidateJwt(token)) {
            StpUtil.logout();
        }
    }


    public String resetConfirm(ConfirmResetReq req) throws Exception {
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + req.getEmail());
        if (code == null) {throw new Exception("验证码不能为空");}
        if (!code.equals(req.getCode())){throw new Exception("验证码错误");}
        return null;
    }

    public void resetEmailAccountPassword(EmailResetReq req) throws Exception {
        String email = req.getEmail();
        ConfirmResetReq confirmReq = converter.convert(req, ConfirmResetReq.class);
        String verify = this.resetConfirm(confirmReq);

        if (verify != null) {return;}
        String password = BCrypt.hashpw(req.getPassword());
        boolean update = userRepository.update().eq("email", email).set("password", password).update();
        //若更新密码成功，则删除redis中的旧数据（验证码）
        if (update) stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
    }
}
