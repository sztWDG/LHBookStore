DROP TABLE
    IF
    EXISTS book;
CREATE TABLE book
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `type_id`     BIGINT         NOT NULL,
    `name`        VARCHAR(99)    NOT NULL,
    `author`      VARCHAR(99)    NOT NULL,
    `publisher`   VARCHAR(99)    NOT NULL,
    `description` VARCHAR(255),
    `isbn`        VARCHAR(16)    NOT NULL,
    `price`       DECIMAL(10, 2) NOT NULL,
    `number`      INT            NOT NULL,
    `cover`       VARCHAR(255),
    `status`      TINYINT        NOT NULL,
    `creator`     BIGINT                  DEFAULT NULL COMMENT '创建者',
    `modifier`    BIGINT                  DEFAULT NULL COMMENT '修改者',
    `create_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    UNIQUE (`isbn`),
    CONSTRAINT `fk_book_type` FOREIGN KEY (`type_id`) REFERENCES `book_type` (`id`) -- 添加外键约束
);