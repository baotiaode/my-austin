package com.my.push.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 短信渠道：真正对接第三方短信平台的逻辑写在这里。
 * 现在用打印模拟，真实项目里这里是调腾讯云/阿里云短信 API（发 HTTP 请求）。
 */
@Component
public class SmsSender implements Sender {

    private static final Logger log = LoggerFactory.getLogger(SmsSender.class);

    @Override
    public Integer getChannel() {
        return 30;   // 30 = 短信，和 message_template.send_channel 对应
    }

    @Override
    public void send(String receiver, String content) {
        // 真实项目：这里发 HTTP 请求调用短信平台
        log.info("【短信渠道】向 {} 发送短信：{}", receiver, content);

    }

    @Override
    public boolean supportsRecall() {
        return false;   // 短信一旦送达，物理上撤不回
    }

}
