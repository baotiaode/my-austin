package com.my.push.domain;

/**
 * 一个接收者 + 他各自替换好的内容。
 * 用 List 而不是 Map，避免重复手机号互相覆盖。
 */
public class ReceiverContent {

    private String receiver;
    private String content;

    public ReceiverContent() {
    }

    public ReceiverContent(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
