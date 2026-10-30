package com.my.push.action;

import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * handler 工位1：丢弃。模板ID在黑名单里就直接丢弃。
 */
@Component
public class DiscardAction implements BusinessProcess {

    // 简化：丢弃名单（模板ID为 99 的丢弃）
    private static final Set<Long> DISCARD_TEMPLATE_IDS = new HashSet<>(Arrays.asList(99L));

    @Override
    public void process(ProcessContext context) {
        TaskInfo taskInfo = (TaskInfo) context.getProcessModel();
        if (DISCARD_TEMPLATE_IDS.contains(taskInfo.getMessageTemplateId())) {
            context.setNeedBreak(true);
            context.setResponse("模板被丢弃");
        }
    }
}
