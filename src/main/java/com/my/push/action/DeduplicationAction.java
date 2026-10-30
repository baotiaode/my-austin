package com.my.push.action;

import com.my.push.domain.ReceiverContent;
import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import com.my.push.service.DedupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * handler 工位3：去重。把"窗口内重复"的接收者从列表移除。
 */
@Component
public class DeduplicationAction implements BusinessProcess {

    private static final Logger log = LoggerFactory.getLogger(DeduplicationAction.class);

    @Autowired
    private DedupService dedupService;

    @Override
    public void process(ProcessContext context) {
        TaskInfo taskInfo = (TaskInfo) context.getProcessModel();

        List<ReceiverContent> before = taskInfo.getReceiverContents();
        List<ReceiverContent> after = before.stream()
                .filter(rc -> !dedupService.isDuplicate(
                        taskInfo.getMessageTemplateId(), rc.getReceiver(), rc.getContent()))
                .collect(Collectors.toList());

        taskInfo.setReceiverContents(after);
        log.info("【去重工位】去重前：{}，去重后：{}", before, after);
    }
}
