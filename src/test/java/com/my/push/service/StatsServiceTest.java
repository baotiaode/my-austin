package com.my.push.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试：启动整个 Spring 容器，测 StatsService（依赖真 MySQL）。
 * 运行前需保证 MySQL / Redis / Kafka 已启动。
 */
@SpringBootTest
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Test
    void testSummary() {
        Map<String, Long> summary = statsService.summary();
        assertNotNull(summary);
        assertNotNull(summary.get("total"));
        assertNotNull(summary.get("success"));
        assertTrue(summary.get("total") >= 0);
    }
}
