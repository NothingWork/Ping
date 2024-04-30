package com.ysu.ping.Entity;

/**
 * @author Yun
 * @description: 返回前端的对象
 */
public class Result {
    private Integer code;// 响应码
    private String message;// 返回消息
    private Object data; // 返回数据

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
