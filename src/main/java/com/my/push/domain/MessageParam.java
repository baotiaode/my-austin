package com.my.push.domain;

import java.util.Map;

/**
 * 一条消息发给谁：一个接收者 + 这个人的模板变量。
 */
public class MessageParam {

    private String receiver;                // 接收者（手机号）
    private Map<String, String> variables;  // 模板变量，如 code -> 123456

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
