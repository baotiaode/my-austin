package com.my.push.action;

import com.my.push.domain.MessageParam;
import com.my.push.domain.MessageTemplate;
import com.my.push.domain.ReceiverContent;
import com.my.push.domain.SendTaskModel;
import com.my.push.domain.TaskInfo;
import com.my.push.mapper.MessageTemplateMapper;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工位2：组装参数。查模板 → 替换占位符 → 组装 TaskInfo。
 */
@Component
public class SendAssembleAction implements BusinessProcess {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel model = (SendTaskModel) context.getProcessModel();

        // 1. 查模板
        MessageTemplate template = messageTemplateMapper.selectById(model.getMessageTemplateId());
        if (template == null) {
            context.setNeedBreak(true);
            context.setResponse("模板不存在");
            return;
        }

        // 1.5 校验模板是否已启用
        if (template.getMsgStatus() == null || template.getMsgStatus() != 30) {
            context.setNeedBreak(true);
            context.setResponse("模板未启用");
            return;
        }

        // 2. 每个接收者各自替换变量，装进列表
        List<ReceiverContent> receiverContents = new ArrayList<>();
        for (MessageParam param : model.getMessageParamList()) {
            // 关键：每次从原始模板开始，避免上一个接收者的替换结果污染下一个
            String content = template.getMsgContent();
            if (param.getVariables() != null) {
                for (Map.Entry<String, String> entry : param.getVariables().entrySet()) {
                    content = content.replace("{$" + entry.getKey() + "}", entry.getValue());
                }
            }
            receiverContents.add(new ReceiverContent(param.getReceiver(), content));
        }

        // 3. 组装 TaskInfo
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setMessageTemplateId(model.getMessageTemplateId());
        taskInfo.setReceiverContents(receiverContents);
        taskInfo.setSendChannel(template.getSendChannel());

        model.setTaskInfo(taskInfo);
    }
}
