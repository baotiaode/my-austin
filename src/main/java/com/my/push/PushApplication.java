package com.my.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动入口。
 * 只有这一个类带 @SpringBootApplication —— 说明这是一个「单体」项目，不是微服务。
 */
@SpringBootApplication
@EnableScheduling   // 开启定时任务（不加这句，@Scheduled 全部静默失效）
public class PushApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushApplication.class, args);
    }
}
