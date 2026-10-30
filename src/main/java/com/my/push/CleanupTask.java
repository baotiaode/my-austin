package com.my.push;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.push.domain.SendRecord;
import com.my.push.mapper.SendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class CleanupTask {

    private static final Logger log = LoggerFactory.getLogger(CleanupTask.class);

    @Autowired
    private SendRecordMapper sendRecordMapper;

    @Scheduled(cron="0 0 3 * * ?")
    public void cleanupExpiredRecords() {
        LocalDateTime deadline = LocalDateTime.now().minusDays(7);
        int deleted = sendRecordMapper.delete(
                new LambdaQueryWrapper<SendRecord>()
                        .lt(SendRecord::getSendTime, deadline)   // send_time < deadline（7天前）
        );
        log.info("【定时清理】删除 {} 条 7 天前的发送记录", deleted);
    }
}
