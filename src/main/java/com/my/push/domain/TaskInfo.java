package com.my.push.domain;

import java.util.List;

/**
 * 组装后的任务：send 链的产出，最终会通过 MQ 发给 handler 链。
 */
public class TaskInfo {

    private Long messageTemplateId;  // 模板ID
    private Integer sendChannel;      // 发送渠道（30=短信）
    private List<ReceiverContent> receiverContents;  // 每个接收者 + 各自替换好的内容
    private int retryCount = 0;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getSendChannel() {
        return sendChannel;
    }

    public void setSendChannel(Integer sendChannel) {
        this.sendChannel = sendChannel;
    }

    public Long getMessageTemplateId() {
        return messageTemplateId;
    }

    public void setMessageTemplateId(Long messageTemplateId) {
        this.messageTemplateId = messageTemplateId;
    }

    public List<ReceiverContent> getReceiverContents() {
        return receiverContents;
    }

    public void setReceiverContents(List<ReceiverContent> receiverContents) {
        this.receiverContents = receiverContents;
    }
}
