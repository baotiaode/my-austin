-- my-austin 初始化脚本（简化版，只保留学习需要的字段）

-- 关键：把客户端字符集设成 utf8mb4，否则下面的中文会乱码
-- （mysql 容器执行这个文件时，默认可能用 latin1 读文件，导致中文双重编码）
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS my_austin DEFAULT CHARACTER SET utf8mb4;

USE my_austin;

CREATE TABLE IF NOT EXISTS message_template (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name         VARCHAR(50)  DEFAULT NULL COMMENT '模板名称',
    send_channel INT          DEFAULT NULL COMMENT '发送渠道：30-短信 40-邮件',
    msg_status   INT          DEFAULT NULL COMMENT '状态：30-启用 10-停用',
    msg_content  VARCHAR(500) DEFAULT NULL COMMENT '消息内容(JSON，含 {$var} 占位符)',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '消息模板';

-- 清掉旧数据再插，避免重复执行产生重复行
DELETE FROM message_template WHERE id IN (1, 2);

INSERT INTO message_template (id, name, send_channel, msg_status, msg_content)
VALUES (1, '验证码短信', 30, 30, '{"content":"您的验证码是{$code}，5分钟内有效"}');

INSERT INTO message_template (id, name, send_channel, msg_status, msg_content)
VALUES (2, '活动通知邮件', 40, 30, '{"content":"尊敬的用户，您报名的活动「{$title}」即将开始"}');
