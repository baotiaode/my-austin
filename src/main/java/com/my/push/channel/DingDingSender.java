package com.my.push.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 钉钉机器人渠道：真实项目里这里调钉钉机器人 webhook。
 * 现在用打印模拟。
 */
@Component
public class DingDingSender implements Sender {

    private static final Logger log = LoggerFactory.getLogger(DingDingSender.class);

    @Override
    public Integer getChannel() {
        return 80;   // 80 = 钉钉机器人
    }

    @Override
    public void send(String receiver, String content) {
        // 真实项目：调钉钉机器人 webhook 发消息
        log.info("【钉钉渠道】向 {} 发送消息：{}", receiver, content);
    }

    @Override
    public boolean supportsRecall() {
        return true;   // 钉钉消息支持撤回
    }

}
