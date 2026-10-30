package com.my.push.Controller;

import com.my.push.service.RecallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecallController {

    @Autowired
    private RecallService recallService;

    @PostMapping("/recall")
    public String recall(@RequestBody Map<String, Long> request) {
        return recallService.recall(request.get("recordId"));
    }
}
