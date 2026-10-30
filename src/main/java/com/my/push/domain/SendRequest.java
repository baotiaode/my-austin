package com.my.push.domain;

import java.util.List;

/**
 * HTTP 请求体：前端发来「用哪个模板、发给哪些人」。
 */
public class SendRequest {

    private Long messageTemplateId;             // 用哪个模板
    private List<MessageParam> messageParamList; // 发给哪些人

    public Long getMessageTemplateId() {
        return messageTemplateId;
    }

    public void setMessageTemplateId(Long messageTemplateId) {
        this.messageTemplateId = messageTemplateId;
    }

    public List<MessageParam> getMessageParamList() {
        return messageParamList;
    }

    public void setMessageParamList(List<MessageParam> messageParamList) {
        this.messageParamList = messageParamList;
    }
}
