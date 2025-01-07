DROP TABLE
    IF
    EXISTS book_type;
CREATE TABLE book_type
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(99) NOT NULL,
    `creator`     BIGINT               DEFAULT NULL COMMENT '创建者',
    `modifier`    BIGINT               DEFAULT NULL COMMENT '修改者',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    UNIQUE (`name`)
);