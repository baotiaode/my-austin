package com.my.push.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.push.domain.TaskInfo;
import com.my.push.pipeline.ProcessContext;
import com.my.push.pipeline.ProcessController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者：从 Kafka 拿到 TaskInfo，启动 handler 链。
 */
@Component
public class MessageReceiver {

    private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProcessController processController;

    @Autowired
    private MessageQueue messageQueue;

    @KafkaListener(topics = "${my-austin.kafka.topic}", groupId = "${my-austin.kafka.consumer-group}")
    public void onMessage(String json) {
        log.info("【消费者】线程[{}] 收到消息：{}", Thread.currentThread().getName(), json);
        TaskInfo taskInfo=null;
        try {
            taskInfo = objectMapper.readValue(json, TaskInfo.class);  // JSON → 对象
            ProcessContext context = new ProcessContext();
            context.setCode("handler");
            context.setProcessModel(taskInfo);
            processController.process(context);
        } catch (Exception e) {
            log.error("【消费者】处理消息失败，json={}", json, e);
            if (taskInfo != null && taskInfo.getRetryCount() < 3) {
                taskInfo.setRetryCount(taskInfo.getRetryCount() + 1);
                messageQueue.send(taskInfo);           // 重新发回原 topic，重试
            } else if (taskInfo != null) {
                messageQueue.sendToDeadLetter(taskInfo); // 超上限，进死信
            } else {
                // 反序列化本身失败，重试也没用，只记录
                log.error("消息反序列化失败：{}", json);
            }

        }
    }

    @KafkaListener(topics="${my-austin.kafka.dead-letter-topic}",groupId = "${my-austin.kafka.dead-letter-group}")
    public void onDeadLetter(String json) {
        log.error("<死信>[{}] <收到消息>{}", Thread.currentThread().getName(), json);
    }
}
