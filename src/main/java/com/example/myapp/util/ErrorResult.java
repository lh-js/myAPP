package com.example.myapp.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResult {
    /**
     * 异常状态码
     */
    private Integer status;

    /**
     * 用户看得见的异常，例如 用户名重复！！,
     */
    private String message;

    /**
     * 异常的名字
     */
    private String exception;

    /**
     * 异常堆栈信息
     */
    private String errors;

    /**
     * 请求路径
     */
    private String requestPath;

    public static ErrorResult fail(Throwable e) {
        return ErrorResult.fail(e, e.getMessage());
    }

    public static ErrorResult fail(Throwable e, String message) {
        ErrorResult result = new ErrorResult();
        result.setMessage(message);
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setException(e.getClass().getName());
        result.setRequestPath(ContextUtil.getRequest().getRequestURI());
        result.setErrors(e.toString());
        return result;
    }
}
