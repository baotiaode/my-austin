package com.my.push.service;

import com.my.push.domain.MessageTemplate;
import com.my.push.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板管理服务：模板的增删改查 + 启用。
 */
@Service
public class MessageTemplateService {

    // 模板状态码（注意：这是 message_template 表的 msg_status，和 send_record 的 status 是两套）
    private static final int STATUS_DRAFT = 10;     // 草稿（不可发送）
    private static final int STATUS_ENABLED = 30;   // 已启用（可发送）
    private static final int STATUS_DISABLED = 40;  // 已停用（不可发送）

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    /** 新增模板：强制设为草稿状态 */
    public MessageTemplate create(MessageTemplate template) {
        template.setMsgStatus(STATUS_DRAFT);
        messageTemplateMapper.insert(template);   // insert 后 id 自动回填
        return template;
    }

    /** 查询所有模板 */
    public List<MessageTemplate> list() {
        return messageTemplateMapper.selectList(null);   // null = 无条件，查全部
    }

    /** 查询单个模板 */
    public MessageTemplate getById(Long id) {
        return messageTemplateMapper.selectById(id);
    }

    /** 修改模板 */
    public boolean update(Long id, MessageTemplate template) {
        template.setId(id);   // 路径里的 id 为准
        return messageTemplateMapper.updateById(template) > 0;
    }

    /** 删除模板 */
    public boolean deleteById(Long id) {
        return messageTemplateMapper.deleteById(id) > 0;
    }

    /** 启用（审核通过）：把草稿改成启用 */
    public boolean enable(Long id) {
        MessageTemplate template = messageTemplateMapper.selectById(id);
        if (template == null) {
            return false;
        }
        template.setMsgStatus(STATUS_ENABLED);
        return messageTemplateMapper.updateById(template) > 0;
    }
}
