package com.my.push.action;

import com.my.push.domain.MessageParam;
import com.my.push.domain.SendTaskModel;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工位1：前置校验。模板ID或参数列表为空就喊停。
 */
@Component
public class SendPreCheckAction implements BusinessProcess {

    @Override
    public void process(ProcessContext context) {
        // 从托盘拿数据（processModel 是 Object，要强转成 SendTaskModel）
        SendTaskModel model = (SendTaskModel) context.getProcessModel();
        Long templateId = model.getMessageTemplateId();
        List<MessageParam> params = model.getMessageParamList();

        // 检查1：模板ID或参数列表为空 → 喊停
        if (templateId == null || params == null || params.isEmpty()) {
            context.setNeedBreak(true);
            context.setResponse("模板ID或参数列表为空");
            return;
        }

        // 检查2：过滤掉 receiver 为空的
        List<MessageParam> validParams = params.stream()
                .filter(p -> p.getReceiver() != null && !p.getReceiver().isEmpty())
                .collect(Collectors.toList());

        if (validParams.isEmpty()) {
            context.setNeedBreak(true);
            context.setResponse("没有有效的接收者");
            return;
        }

        // 通过：把过滤后的干净数据写回托盘
        model.setMessageParamList(validParams);
    }
}
