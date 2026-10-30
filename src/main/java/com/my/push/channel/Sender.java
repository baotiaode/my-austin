package com.my.push.channel;

/**
 * 发送渠道抽象：每个渠道（短信/邮件/push...）都实现这个接口。
 */
public interface Sender {

    /** 渠道类型，和数据库 send_channel 对应（30=短信，40=邮件） */
    Integer getChannel();

    /** 向单个接收者发送内容 */
    void send(String receiver, String content);

    /** 该渠道是否支持撤回（短信不支持，邮件/IM 等支持） */
    boolean supportsRecall();
}
