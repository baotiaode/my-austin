package com.my.push.vo;

/**
 * 统一返回结构：所有接口都返回 {code, msg, data}。
 * T 是泛型，data 可以是任何类型。
 */
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    // 构造器私有：逼调用方用下面的静态工厂方法，而不是 new Result(...)
    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /** 成功（带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /** 成功（无数据） */
    public static <T> Result<T> success() {
        return success(null);
    }

    /** 失败 */
    public static <T> Result<T> fail(String msg) {
        return new Result<>(500, msg, null);
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
