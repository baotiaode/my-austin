package com.my.push.domain;

import java.util.List;

/**
 * send 链上传递的模型：装输入参数 + 组装结果。
 */
public class SendTaskModel {

    private Long messageTemplateId;             // 模板ID（输入）
    private List<MessageParam> messageParamList; // 接收者参数（输入）
    private TaskInfo taskInfo;                   // 组装结果（输出，工位2填）

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

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }
}
