package com.my.push.Controller;

import com.my.push.domain.SendRequest;
import com.my.push.service.SendService;
import com.my.push.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 入口：接收发送请求，交给 SendService。
 */
@RestController
public class PushController {

    @Autowired
    private SendService sendService;

    @PostMapping("/send")
    public Result<String> send(@RequestBody SendRequest request) {
        return sendService.send(request);
    }
}
