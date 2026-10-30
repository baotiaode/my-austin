package com.my.push.action;

import com.my.push.domain.ReceiverContent;
import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import com.my.push.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * handler 工位：限流。把窗口内超过频次上限的接收者从列表移除。
 */
@Component
public class RateLimitAction implements BusinessProcess {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAction.class);

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public void process(ProcessContext context) {
        TaskInfo taskInfo = (TaskInfo) context.getProcessModel();

        List<ReceiverContent> before = taskInfo.getReceiverContents();
        List<ReceiverContent> after = before.stream()
                .filter(rc -> !rateLimitService.isLimited(
                        taskInfo.getMessageTemplateId(), rc.getReceiver()))
                .collect(Collectors.toList());

        taskInfo.setReceiverContents(after);
        log.info("【限流工位】限流前：{}，限流后：{}", before, after);
    }
}
