/*
 * Copyright (c) 2023.
 * Project: user-center-realend
 * File: BaseResponse.java
 * Last date: 2023/11/18 下午5:36
 * Developer: KingYen
 *
 * Created by KingYen on 2023/11/18 17:36:46.
 */

package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    // 相应状态码 0 - success, other - failureure
    private int code;

    // 相应状态 ok - success, other - failure
    private String message;

    // 描述
    private String description;

    // 返回数据
    private T data;

    public BaseResponse(int code, String message, T data, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, message, data, "");
    }

    public BaseResponse(int code, T data) {
        this(code, "", data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null, errorCode.getDescription());
    }
}
