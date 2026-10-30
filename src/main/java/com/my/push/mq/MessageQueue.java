package com.my.push.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.push.domain.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息生产者：把 TaskInfo 序列化成 JSON，投递到 Kafka。
 */
@Component
public class MessageQueue {

    private static final Logger log = LoggerFactory.getLogger(MessageQueue.class);

    /** topic 从配置文件读取，改环境不用改代码 */
    @Value("${my-austin.kafka.topic}")
    private String topic;

    @Value("${my-austin.kafka.dead-letter-topic}")
    private String deadLetterTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendToDeadLetter(TaskInfo taskInfo){
        try {
            String json = objectMapper.writeValueAsString(taskInfo);  // 对象 → JSON 字符串
            kafkaTemplate.send(deadLetterTopic, json);                          // 发到 Kafka
            log.info("【生产者】已投递到 Kafka topic={}，消息：{}", deadLetterTopic, json);
        } catch (Exception e) {
            log.error("【生产者】投递 Kafka 失败，taskInfo={}", taskInfo, e);
        }
    }

    public void send(TaskInfo taskInfo) {
        try {
            String json = objectMapper.writeValueAsString(taskInfo);  // 对象 → JSON 字符串
            kafkaTemplate.send(topic, json);                          // 发到 Kafka
            log.info("【生产者】已投递到 Kafka topic={}，消息：{}", topic, json);
        } catch (Exception e) {
            log.error("【生产者】投递 Kafka 失败，taskInfo={}", taskInfo, e);
        }
    }
}
