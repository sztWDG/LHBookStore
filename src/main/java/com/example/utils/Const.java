package com.example.utils;

/**
 * 一些常量字符串整合
 */
public final class Const {
    //JWT令牌
    public final static String JWT_BLACK_LIST = "jwt:blacklist:";
    public final static String JWT_FREQUENCY = "jwt:frequency:";
    //请求频率限制
    public final static String FLOW_LIMIT_COUNTER = "flow:counter:";
    public final static String FLOW_LIMIT_BLOCK = "flow:block:";
    //邮件验证码
    public final static String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public final static String VERIFY_EMAIL_DATA = "verify:email:data:";
    //过滤器优先级
    public final static int ORDER_FLOW_LIMIT = -101;
    public final static int ORDER_CORS = -102;
    //消息队列
    public final static String MQ_MAIL = "mail";
    //用户角色
    public final static Integer ROLE_DEFAULT = 3;
    public final static Integer ROLE_PHOTOGRAPHER = 2;
    //购物车;
    public final static String SHOPPING_CART_PRE = "shopping_cart:userId_";
    public final static String USER_BLACK_LIST = "user:blacklist:";

}