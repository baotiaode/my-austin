package com.my.push.exception;

import com.my.push.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：兜住所有 controller 抛出的异常，返回友好的统一结构。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)   // 兜住所有异常
    public Result<Void> handle(Exception e) {
        log.error("系统异常", e);
        return Result.fail("系统异常：" + e.getMessage());
    }
}
