package com.my.push.channel;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道分发器：启动时把所有 Sender 收进来，按渠道号建立映射。
 */
@Component
public class SenderHolder {

    private final Map<Integer, Sender> senderMap = new HashMap<>();

    // Spring 会自动把"容器里所有实现了 Sender 的 bean"注入到这个 List
    public SenderHolder(List<Sender> senders) {
        for (Sender sender : senders) {
            senderMap.put(sender.getChannel(), sender);
        }
    }

    public Sender get(Integer channel) {
        return senderMap.get(channel);
    }
}
