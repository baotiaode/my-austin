package com.my.push.action;

import com.my.push.domain.ReceiverContent;
import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * handler 工位4：敏感词过滤。任一接收者的内容含敏感词就喊停。
 */
@Component
public class SensWordsAction implements BusinessProcess {

    private static final List<String> SENS_WORDS = Arrays.asList("傻逼", "垃圾");

    @Override
    public void process(ProcessContext context) {
        TaskInfo taskInfo = (TaskInfo) context.getProcessModel();
        for (ReceiverContent rc : taskInfo.getReceiverContents()) {
            for (String word : SENS_WORDS) {
                if (rc.getContent().contains(word)) {
                    context.setNeedBreak(true);
                    context.setResponse("内容含敏感词：" + word);
                    return;
                }
            }
        }
    }
}
