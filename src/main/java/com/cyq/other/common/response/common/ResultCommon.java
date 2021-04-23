package com.cyq.other.common.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * API统一返回接口封装实体类
 */
@Data
@AllArgsConstructor
public class ResultCommon<T> implements Serializable {
    // 状态码
    private Integer code;
    // 响应消息
    private String message;
    // 响应数据
    private T data;

    private ResultCommon(ResultCode resultCode, T data) {
        this.code =resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    /* 成功 */
    public static <T> ResultCommon<T> success(ResultCode resultCode, T data) {
        return success(resultCode.code(), resultCode.message(), data);
    }

    public static <T> ResultCommon<T> success(Integer code, String message, T data) {
        return new ResultCommon(code, message, data);
    }

    /* 失败 */
    public static <T> ResultCommon<T> failure(ResultCode resultCode, T data) {
        return failure(resultCode.code(), resultCode.message(), data);
    }

    public static <T> ResultCommon<T> failure(Integer code, String message, T data) {
        return new ResultCommon(code, message, data);
    }

}
