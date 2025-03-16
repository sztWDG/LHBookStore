DROP TABLE
    IF
    EXISTS `order`;
CREATE TABLE `order`
(
    `id`                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id`            VARCHAR(255) NOT NULL,
    `user_id`             BIGINT       NOT NULL,
    `status`              TINYINT NOT NULL,
    `book_id`             BIGINT       NOT NULL,
    `bought_number`       INT          NOT NULL,
    `total_price` DOUBLE NOT NULL,
    `shipping_address_id` BIGINT       NOT NULL,
    `order_time` timestamp NOT NULL,
    `pay_time` timestamp NULL DEFAULT NULL,
    `delivery_time` timestamp NULL DEFAULT NULL,
    `shipping_time` timestamp NULL DEFAULT NULL,
    `finish_time` timestamp NULL DEFAULT NULL,
    `created_by`          BIGINT       NOT NULL,
    `modified_by`         BIGINT
);