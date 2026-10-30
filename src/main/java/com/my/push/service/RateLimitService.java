package com.my.push.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 限流服务：用 Redis ZSET 做滑动窗口限流。
 * 规则：同一个接收者在同一个模板下，窗口时间内最多收 maxCount 条。
 */
@Service
public class RateLimitService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /** 窗口时间（秒），从配置文件读，缺省 60 */
    @Value("${my-austin.limit.window-seconds:60}")
    private long windowSeconds;

    /** 窗口内最大条数，从配置文件读，缺省 3 */
    @Value("${my-austin.limit.max-count:3}")
    private long maxCount;

    /**
     * 限流 Lua：
     * 删除窗口外旧记录 -> 数窗口内条数 -> 超上限返回1，否则记一条返回0
     */
    private static final DefaultRedisScript<Long> LIMIT_SCRIPT = new DefaultRedisScript<>(
            "local key = KEYS[1]\n" +
                    "local now = tonumber(ARGV[1])\n" +
                    "local window = tonumber(ARGV[2])\n" +
                    "local maxCount = tonumber(ARGV[3])\n" +
                    "redis.call('ZREMRANGEBYSCORE', key, 0, now - window)\n" +
                    "local count = redis.call('ZCARD', key)\n" +
                    "if count >= maxCount then\n" +
                    "    return 1\n" +
                    "end\n" +
                    "redis.call('ZADD', key, now, now)\n" +
                    "return 0",
            Long.class);

    /**
     * 是否被限流。true = 超过上限（要拦住），false = 放行。
     */
    public boolean isLimited(Long templateId, String receiver) {
        String key = "limit:" + templateId + ":" + receiver;
        long now = System.currentTimeMillis();

        Long result = redisTemplate.execute(
                LIMIT_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(now),
                String.valueOf(windowSeconds * 1000),   // 秒转毫秒
                String.valueOf(maxCount));

        return result != null && result == 1;
    }
}
