package com.example.myapp.advice;

import com.example.myapp.exception.NoDataFoundException;
import com.example.myapp.util.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //告诉浏览器这是个内部异常。否则浏览器会识别成200正常
    @ExceptionHandler(Throwable.class)
    public ErrorResult handleThrowable(Throwable e) {
        ErrorResult error = ErrorResult.fail(e);
        e.printStackTrace();
        return error;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoDataFoundException.class)
    public ErrorResult handleNoDataFound(NoDataFoundException e) {
        String message = StringUtils.isEmpty(e.getMessage()) ? "未获取到您查询的数据，请重新确认。" : e.getMessage();
        ErrorResult error = ErrorResult.fail(e, message);
        e.printStackTrace();
        return error;
    }
}
