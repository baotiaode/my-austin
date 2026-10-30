package com.my.push.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 企业微信渠道：真实项目里这里调企业微信 API（发应用消息）。
 * 现在用打印模拟。
 */
@Component
public class EnterpriseWeChatSender implements Sender {

    private static final Logger log = LoggerFactory.getLogger(EnterpriseWeChatSender.class);

    @Override
    public Integer getChannel() {
        return 70;   // 70 = 企业微信
    }

    @Override
    public void send(String receiver, String content) {
        // 真实项目：调企业微信 API 发消息
        log.info("【企业微信渠道】向 {} 发送消息：{}", receiver, content);
    }

    @Override
    public boolean supportsRecall() {
        return true;   // 企业微信消息支持撤回
    }

}
