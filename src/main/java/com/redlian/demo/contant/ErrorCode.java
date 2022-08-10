/*
 * Copyright (c) 2010-2020 MOI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of MOI.
 */
package com.redlian.demo.contant;

public enum ErrorCode {
    CONNEC_ERROR("9998", "Connection Error"), RESPONSE_ERROR("9999", "Response Error");

    private String code;
    private String msg;

    ErrorCode(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
