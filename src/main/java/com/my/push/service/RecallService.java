package com.my.push.service;

import com.my.push.channel.Sender;
import com.my.push.channel.SenderHolder;
import com.my.push.domain.SendRecord;
import com.my.push.mapper.SendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 撤回服务：在撤回窗口内、且渠道支持撤回时，把已发送的消息撤回。
 */
@Service
public class RecallService {

    private static final Logger log = LoggerFactory.getLogger(RecallService.class);

    // 状态码（和 SendMessageAction 里的 10/20/30 保持一致）
    private static final int STATUS_SUCCESS = 20;    // 发送成功
    private static final int STATUS_RECALLED = 40;   // 已撤回

    /** 撤回窗口（秒），从配置文件读，缺省 60 */
    @Value("${my-austin.recall.window-seconds:60}")
    private long windowSeconds;

    @Autowired
    private SendRecordMapper sendRecordMapper;

    @Autowired
    private SenderHolder senderHolder;

    /**
     * 撤回一条发送记录，返回结果说明。
     */
    public String recall(Long recordId) {
        if (recordId == null) {
            return "recordId 不能为空";
        }

        // 1. 找到这条记录
        SendRecord record = sendRecordMapper.selectById(recordId);
        if (record == null) {
            return "记录不存在";
        }

        // 2. 只有"发送成功"的消息才能撤回
        if (record.getStatus() == null || record.getStatus() != STATUS_SUCCESS) {
            return "只有发送成功的消息才能撤回";
        }

        // 3. 是否还在撤回窗口内
        long seconds = Duration.between(record.getSendTime(), LocalDateTime.now()).getSeconds();
        if (seconds > windowSeconds) {
            return "超过撤回窗口";
        }

        // 4. 渠道是否支持撤回（短信撤不回）
        Sender sender = senderHolder.get(record.getSendChannel());
        if (sender == null || !sender.supportsRecall()) {
            return "该渠道不支持撤回";
        }

        // 5. 撤回：真实项目这里要先调渠道的撤回 API，成功后才改状态
        record.setStatus(STATUS_RECALLED);
        sendRecordMapper.updateById(record);
        log.info("【撤回】记录 {} 已撤回", recordId);
        return "撤回成功";
    }
}
