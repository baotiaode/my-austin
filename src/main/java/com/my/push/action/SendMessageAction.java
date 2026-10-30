package com.my.push.action;

import com.my.push.channel.Sender;
import com.my.push.channel.SenderHolder;
import com.my.push.domain.ReceiverContent;
import com.my.push.domain.SendRecord;
import com.my.push.domain.TaskInfo;
import com.my.push.mapper.SendRecordMapper;
import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import com.my.push.service.DedupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * handler 工位5：按渠道分发发送，并旁路写入发送记录。
 */
@Component
public class SendMessageAction implements BusinessProcess {

    private static final Logger log = LoggerFactory.getLogger(SendMessageAction.class);

    private static final int STATUS_SENDING = 10;
    private static final int STATUS_SUCCESS = 20;
    private static final int STATUS_FAILED = 30;

    @Autowired
    private SenderHolder senderHolder;

    @Autowired
    private SendRecordMapper sendRecordMapper;

    @Autowired
    private DedupService dedupService;

    @Override
    public void process(ProcessContext context) {
        TaskInfo taskInfo = (TaskInfo) context.getProcessModel();

        Sender sender = senderHolder.get(taskInfo.getSendChannel());
        if (sender == null) {
            log.warn("【发送】未知渠道：{}", taskInfo.getSendChannel());
            return;
        }

        for (ReceiverContent rc : taskInfo.getReceiverContents()) {
            SendRecord record = new SendRecord();
            record.setTemplateId(taskInfo.getMessageTemplateId());
            record.setReceiver(rc.getReceiver());
            record.setContent(rc.getContent());
            record.setSendChannel(taskInfo.getSendChannel());
            record.setSendTime(LocalDateTime.now());
            record.setStatus(STATUS_SENDING);
            sendRecordMapper.insert(record);

            try {
                sender.send(rc.getReceiver(), rc.getContent());
                record.setStatus(STATUS_SUCCESS);
                sendRecordMapper.updateById(record);
                dedupService.markSent(taskInfo.getMessageTemplateId(), rc.getReceiver(), rc.getContent());
            } catch (RuntimeException e) {
                record.setStatus(STATUS_FAILED);
                sendRecordMapper.updateById(record);
                throw e;
            }
        }
    }
}
