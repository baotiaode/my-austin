package com.my.push.action;

import com.my.push.pipeline.BusinessProcess;
import com.my.push.pipeline.ProcessContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * handler 工位2：夜间屏蔽。
 * 当前时间落在屏蔽时段内时，整条消息不发（中断链）。
 */
@Component
public class ShieldAction implements BusinessProcess {

    private static final Logger log = LoggerFactory.getLogger(ShieldAction.class);

    /** 屏蔽开始小时（如 22 点），从配置文件读，缺省 22 */
    @Value("${my-austin.shield.start-hour:22}")
    private int startHour;

    /** 屏蔽结束小时（如 8 点，即次日 8 点），从配置文件读，缺省 8 */
    @Value("${my-austin.shield.end-hour:8}")
    private int endHour;

    @Override
    public void process(ProcessContext context) {
        int hour = LocalTime.now().getHour();

        boolean inShield;
        if (startHour < endHour) {
            // 不跨天：如 13~15 点
            inShield = hour >= startHour && hour < endHour;
        } else {
            // 跨天：如 22~次日8 点
            inShield = hour >= startHour || hour < endHour;
        }

        if (inShield) {
            log.info("【屏蔽工位】当前 {} 点，处于夜间屏蔽时段({}~{})，消息被屏蔽",
                    hour, startHour, endHour);
            context.setNeedBreak(true);
            context.setResponse("夜间屏蔽");
        }
    }
}
