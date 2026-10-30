package com.my.push.action;

import com.my.push.domain.SendTaskModel;
import com.my.push.domain.TaskInfo;
import com.my.push.mq.MessageQueue;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工位4：发送到 MQ。把组装好的 TaskInfo 投到消息队列，send 链到此结束。
 */
@Component
public class SendMqAction implements BusinessProcess {

    @Autowired
    private MessageQueue messageQueue;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel model = (SendTaskModel) context.getProcessModel();
        TaskInfo taskInfo = model.getTaskInfo();

        // 把任务投到 MQ，send 链使命完成
        messageQueue.send(taskInfo);
    }
}
