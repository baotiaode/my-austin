-- 发送记录表（M6 新增）：每次发送后写入一条记录
SET NAMES utf8mb4;

USE my_austin;

CREATE TABLE IF NOT EXISTS send_record (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    template_id  BIGINT       DEFAULT NULL COMMENT '模板ID',
    receiver     VARCHAR(50)  DEFAULT NULL COMMENT '接收者',
    content      VARCHAR(500) DEFAULT NULL COMMENT '发送内容',
    send_channel INT          DEFAULT NULL COMMENT '发送渠道',
    send_time    DATETIME     DEFAULT NULL COMMENT '发送时间',
    status       INT          DEFAULT NULL COMMENT '状态：1-成功 0-失败',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '发送记录';
