package com.my.push.pipeline;

import java.util.List;
import java.util.Map;

public class ProcessController {
    private Map<String,ProcessTemplate> templateConfig;
    public void setTemplateConfig(Map<String, ProcessTemplate> templateConfig) {
        this.templateConfig = templateConfig;
    }

    public void process(ProcessContext context) {
        ProcessTemplate template=templateConfig.get(context.getCode());

        List<BusinessProcess> processList=template.getProcessList();

        for(BusinessProcess businessProcess:processList){
            businessProcess.process(context);

            if(Boolean.TRUE.equals(context.getNeedBreak())){
                break;
            }
        }

    }
}
