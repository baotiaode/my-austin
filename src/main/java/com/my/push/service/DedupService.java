package com.my.push.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 去重服务：用 Redis ZSET 做内容去重。
 * 同一个接收者，在窗口时间内收到过相同内容 → 判定重复。
 */
@Service
public class DedupService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 窗口：60 秒
    private static final long WINDOW_MILLIS = 60 * 1000;

    private static final DefaultRedisScript<Long> CHECK_SCRIPT = new DefaultRedisScript<>(
            "local key = KEYS[1]\n" +
                    "local content = ARGV[1]\n" +
                    "local now = tonumber(ARGV[2])\n" +
                    "local window = tonumber(ARGV[3])\n" +
                    "redis.call('ZREMRANGEBYSCORE', key, 0, now - window)\n" +
                    "local score = redis.call('ZSCORE', key, content)\n" +
                    "if score then\n" +
                    "    return 1\n" +
                    "end\n" +
                    "return 0",
            Long.class);   // 返回值类型：1=重复，0=放行

    private static final DefaultRedisScript<Long> MARK_SCRIPT = new DefaultRedisScript<>(
            "local key = KEYS[1]\n" +
                    "local content = ARGV[1]\n" +
                    "local now = tonumber(ARGV[2])\n" +
                    "local window = tonumber(ARGV[3])\n" +
                    "redis.call('ZREMRANGEBYSCORE', key, 0, now - window)\n" +
                    "redis.call('ZADD', key, now, content)\n" +
                    "return 0",
            Long.class);

    /**
     * 是否重复。true = 重复（要跳过），false = 第一次（放行）。
     */
    public boolean isDuplicate(Long templateId, String receiver, String content) {
        String key = "dedup:" + templateId + ":" + receiver;
        long now = System.currentTimeMillis();

        Long result = redisTemplate.execute(
                CHECK_SCRIPT,
                Collections.singletonList(key),          // KEYS
                content,                                  // ARGV[1]
                String.valueOf(now),                      // ARGV[2]
                String.valueOf(WINDOW_MILLIS));           // ARGV[3]

        return result != null && result == 1;   // 1 = 重复
    }

    public void markSent(Long templateId, String receiver, String content) {
        String key = "dedup:" + templateId + ":" + receiver;
        long now = System.currentTimeMillis();

        redisTemplate.execute(
                MARK_SCRIPT,
                Collections.singletonList(key),
                content,
                String.valueOf(now),
                String.valueOf(WINDOW_MILLIS));
    }
}
