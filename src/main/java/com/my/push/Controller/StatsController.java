package com.my.push.Controller;

import com.my.push.service.StatsService;
import com.my.push.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/summary")
    public Result<Map<String, Long>> summary() {
        return Result.success(statsService.summary());
    }

    @GetMapping("/by-channel")
    public Result<List<Map<String, Object>>> byChannel() {
        return Result.success(statsService.byChannel());
    }
}
