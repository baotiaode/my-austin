package com.my.push.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 的纯单元测试：不依赖 Spring，直接调静态工厂方法。
 */
class ResultTest {

    @Test
    void testSuccessWithData() {
        Result<String> r = Result.success("hello");
        assertEquals(200, r.getCode());
        assertEquals("success", r.getMsg());
        assertEquals("hello", r.getData());
    }

    @Test
    void testSuccessWithoutData() {
        Result<Void> r = Result.success();
        assertEquals(200, r.getCode());
        assertNull(r.getData());
    }

    @Test
    void testFail() {
        Result<Object> r = Result.fail("出错了");
        assertEquals(500, r.getCode());
        assertEquals("出错了", r.getMsg());
        assertNull(r.getData());
    }
}
