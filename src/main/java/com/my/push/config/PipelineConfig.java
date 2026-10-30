package com.my.push.config;

import com.my.push.action.*;
import com.my.push.pipeline.ProcessController;
import com.my.push.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 责任链装配：把两条链（send / handler）注册进 ProcessController。
 */
@Configuration
public class PipelineConfig {

    @Bean
    public ProcessTemplate sendTemplate(SendPreCheckAction preCheck,
                                        SendAssembleAction assemble,
                                        SendAfterCheckAction afterCheck,
                                        SendMqAction mq) {
        ProcessTemplate template = new ProcessTemplate();
        template.setProcessList(Arrays.asList(preCheck, assemble, afterCheck, mq));
        return template;
    }

    @Bean
    public ProcessTemplate handlerTemplate(DiscardAction discard,
                                           ShieldAction shield,
                                           DeduplicationAction dedup,
                                           RateLimitAction rateLimit,
                                           SensWordsAction sensWords,
                                           SendMessageAction sendMessage) {
        ProcessTemplate template = new ProcessTemplate();
        template.setProcessList(Arrays.asList(discard, shield, dedup, rateLimit, sensWords, sendMessage));
        return template;
    }

    @Bean
    public ProcessController processController(ProcessTemplate sendTemplate,
                                               ProcessTemplate handlerTemplate) {
        Map<String, ProcessTemplate> templateConfig = new HashMap<>();
        templateConfig.put("send", sendTemplate);
        templateConfig.put("handler", handlerTemplate);
        ProcessController processController = new ProcessController();
        processController.setTemplateConfig(templateConfig);
        return processController;
    }
}
