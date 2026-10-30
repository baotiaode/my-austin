package com.my.push.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("message_template")
public class MessageTemplate {
    @TableId(type = IdType.AUTO)
    private Long id;              // 对应列 id

    private String name;          // 对应列 name
    private Integer sendChannel;  // 对应列 send_channel（发送渠道）
    private Integer msgStatus;    // 对应列 msg_status（状态）
    private String msgContent;    // 对应列 msg_content（内容JSON）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSendChannel() {
        return sendChannel;
    }

    public void setSendChannel(Integer sendChannel) {
        this.sendChannel = sendChannel;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
