package com.my.push.Controller;

import com.my.push.domain.MessageTemplate;
import com.my.push.service.MessageTemplateService;
import com.my.push.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @GetMapping
    public Result<List<MessageTemplate>> list() {
        return Result.success(messageTemplateService.list());
    }

    @GetMapping("/{id}")
    public Result<MessageTemplate> getById(@PathVariable Long id) {
        return Result.success(messageTemplateService.getById(id));
    }

    @PostMapping
    public Result<MessageTemplate> create(@RequestBody MessageTemplate template) {
        return Result.success(messageTemplateService.create(template));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody MessageTemplate template) {
        return Result.success(messageTemplateService.update(id, template));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return Result.success(messageTemplateService.deleteById(id));
    }

    @PostMapping("/{id}/enable")
    public Result<Boolean> enable(@PathVariable Long id) {
        return Result.success(messageTemplateService.enable(id));
    }
}
