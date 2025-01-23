DROP TABLE
    IF
    EXISTS shipping_address;
CREATE TABLE shipping_address
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(99)  NOT NULL,
    `phone`       VARCHAR(13)  NOT NULL,
    `address`     VARCHAR(255) NOT NULL,
    `user_id`     BIGINT       NOT NULL,
    `creator`     BIGINT                DEFAULT NULL COMMENT '创建者',
    `modifier`    BIGINT                DEFAULT NULL COMMENT '修改者',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) -- 添加外键约束
);

