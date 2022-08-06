package com.redlian.demo.exception;

import org.springframework.http.HttpStatus;

import com.redlian.demo.contant.ErrorCode;

public class DemoException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String msg;

    public DemoException(final ErrorCode errorCode, final Exception e) {
        super(e);
        this.msg = errorCode.getMsg();
    }

    public DemoException(final String msg, final HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}
