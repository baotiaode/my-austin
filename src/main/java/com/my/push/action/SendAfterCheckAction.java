package com.my.push.action;

import com.my.push.domain.SendTaskModel;
import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.stereotype.Component;

/**
 * 工位3：后置校验。检查组装结果是否完整。
 */
@Component
public class SendAfterCheckAction implements BusinessProcess {

    @Override
    public void process(ProcessContext context) {
        SendTaskModel model = (SendTaskModel) context.getProcessModel();
        TaskInfo taskInfo = model.getTaskInfo();

        // 组装结果为空，或没有接收者 → 喊停
        if (taskInfo == null || taskInfo.getReceiverContents() == null || taskInfo.getReceiverContents().isEmpty()) {
            context.setNeedBreak(true);
            context.setResponse("组装结果不完整");
            return;
        }
        // 检查通过：什么都不用做，链自然继续往下走
    }
}
