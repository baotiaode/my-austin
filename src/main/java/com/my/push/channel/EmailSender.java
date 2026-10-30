package com.my.push.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 邮件渠道：真正对接邮件服务（SMTP / 邮件平台）的逻辑写在这里。
 * 现在用打印模拟，真实项目里这里是调邮件服务发 HTTP 请求。
 */
@Component
public class EmailSender implements Sender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    @Override
    public Integer getChannel() {
        return 40;   // 40 = 邮件，和 message_template.send_channel 对应
    }

    @Override
    public void send(String receiver, String content) {
        // 真实项目：这里发 HTTP 请求调用邮件服务
        log.info("【邮件渠道】向 {} 发送邮件：{}", receiver, content);
    }

    @Override
    public boolean supportsRecall() {
        return true;   // 简化：邮件支持撤回（真实项目里要调邮件服务撤回 API）
    }

}
