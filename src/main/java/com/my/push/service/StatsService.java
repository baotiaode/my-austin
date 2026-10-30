package com.my.push.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.push.domain.SendRecord;
import com.my.push.mapper.SendRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计：对 send_record 里的数据做汇总。
 */
@Service
public class StatsService {

    private static final int STATUS_SENDING = 10;   // 发送中
    private static final int STATUS_SUCCESS = 20;   // 成功
    private static final int STATUS_FAILED = 30;    // 失败
    private static final int STATUS_RECALLED = 40;  // 已撤回

    @Autowired
    private SendRecordMapper sendRecordMapper;

    /** 总体统计：总数 / 成功 / 失败 / 撤回 */
    public Map<String, Long> summary() {
        long total = sendRecordMapper.selectCount(null);   // null = 无条件，数全表

        long success = sendRecordMapper.selectCount(
                new LambdaQueryWrapper<SendRecord>().eq(SendRecord::getStatus, STATUS_SUCCESS));
        long failed = sendRecordMapper.selectCount(
                new LambdaQueryWrapper<SendRecord>().eq(SendRecord::getStatus, STATUS_FAILED));
        long recalled = sendRecordMapper.selectCount(
                new LambdaQueryWrapper<SendRecord>().eq(SendRecord::getStatus, STATUS_RECALLED));

        // LinkedHashMap 保证 key 顺序固定（普通 HashMap 顺序会乱）
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("success", success);
        result.put("failed", failed);
        result.put("recalled", recalled);
        return result;
    }

    /** 按渠道分组统计：每个渠道各发了多少条 */
    public List<Map<String, Object>> byChannel() {
        return sendRecordMapper.selectMaps(
                new QueryWrapper<SendRecord>()
                        .select("send_channel", "count(*) as cnt")
                        .groupBy("send_channel"));
    }
}
