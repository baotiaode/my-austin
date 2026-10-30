package com.my.push.service;

import com.my.push.domain.SendRequest;
import com.my.push.domain.SendTaskModel;
import com.my.push.pipeline.ProcessContext;
import com.my.push.pipeline.ProcessController;
import com.my.push.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送服务：接收请求，组装托盘，触发 send 链。
 */
@Service
public class SendService {

    @Autowired
    private ProcessController processController;

    public Result<String> send(SendRequest request) {
        // 1. 组装托盘数据（把请求塞进 SendTaskModel）
        SendTaskModel model = new SendTaskModel();
        model.setMessageTemplateId(request.getMessageTemplateId());
        model.setMessageParamList(request.getMessageParamList());

        // 2. 创建托盘，指定走 send 链
        ProcessContext context = new ProcessContext();
        context.setCode("send");
        context.setProcessModel(model);

        // 3. 触发流水线（核心！）
        processController.process(context);

        // 4. 根据有没有喊停，返回统一结果
        if (Boolean.TRUE.equals(context.getNeedBreak())) {
            return Result.fail(String.valueOf(context.getResponse()));
        }
        return Result.success("发送成功");
    }
}
