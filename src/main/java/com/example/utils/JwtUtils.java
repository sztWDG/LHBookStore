package com.example.utils;


import cn.dev33.satoken.jwt.SaJwtTemplate;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用于处理Jwt令牌的工具类
 */
@Component
public class JwtUtils {

    //为用户生成Jwt令牌的冷却时间，防止刷接口频繁登录生成令牌，以秒为单位
    @Value("${spring.security.jwt.limit.base}")
    private int limit_base;
    //用户如果继续恶意刷令牌，更严厉的封禁时间
    @Value("${spring.security.jwt.limit.upgrade}")
    private int limit_upgrade;
    //判定用户在冷却时间内，继续恶意刷令牌的次数
    @Value("${spring.security.jwt.limit.frequency}")
    private int limit_frequency;

    @Resource
    StringRedisTemplate template;

    @Resource
    FlowUtils utils;


    /**
     * 让指定Jwt令牌失效
     *
     * @param headerToken 请求头中携带的令牌
     * @return 是否操作成功
     */
    public boolean invalidateJwt(String headerToken) {
        try {
            JWT jwt = JWT.of(headerToken);
            JSONObject payloads = jwt.getPayloads();
            return deleteToken(jwt.getPayload("jwtId").toString(), payloads.getLong("eff"));
        } catch (Exception e) {
            return false;
        }
    }

    @PostConstruct
    public void setSaJwtTemplate() {
        SaJwtUtil.setSaJwtTemplate(new SaJwtTemplate() {
            @Override
            public String generateToken(JWT jwt, String keyt) {
                int loginId = Integer.parseInt(jwt.getPayload("loginId").toString());
                if (frequencyCheck(loginId)) {
                    jwt.setPayload("jwtId", UUID.randomUUID().toString());
                    return super.generateToken(jwt, keyt);
                }
                return null;
            }

            @Override
            public JWT parseToken(String token, String loginType, String keyt, boolean isCheckTimeout) {
                JWT jwt = JWT.of(token);
                if (isInvalidToken(jwt.getPayload("jwtId").toString())) return null;
                if (isInvalidUser(Integer.parseInt(jwt.getPayload("loginId").toString()))) return null;
                return super.parseToken(token, loginType, keyt, isCheckTimeout);
            }

        });
    }

    /**
     * 频率检测，防止用户高频申请Jwt令牌，并且采用阶段封禁机制
     * 如果已经提示无法登录的情况下用户还在刷，那么就封禁更长时间
     *
     * @param userId 用户ID
     * @return 是否通过频率检测
     */
    private boolean frequencyCheck(int userId) {
        String key = Const.JWT_FREQUENCY + userId;
        return utils.limitOnceUpgradeCheck(key, limit_frequency, limit_base, limit_upgrade);
    }


    /**
     * 将Token列入Redis黑名单中
     *
     * @param uuid    令牌ID
     * @param effTime 过期时间
     * @return 是否操作成功
     */
    private boolean deleteToken(String uuid, Long effTime) throws Exception {
        if (this.isInvalidToken(uuid))
            throw new Exception("已在黑名单中");
        long expire = Math.max(effTime - System.currentTimeMillis(), 0);
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 验证Token是否被列入Redis黑名单
     *
     * @param uuid 令牌ID
     * @return 是否操作成功
     */
    private boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }


    public void deleteUser(int uid) {
        template.opsForValue().set(Const.USER_BLACK_LIST + uid, "", 72, TimeUnit.HOURS);
    }

    private boolean isInvalidUser(int uid) {
        return Boolean.TRUE.equals(template.hasKey(Const.USER_BLACK_LIST + uid));
    }
}
