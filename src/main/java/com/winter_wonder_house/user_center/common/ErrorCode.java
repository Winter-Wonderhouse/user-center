/*
 * Copyright (c) 2023.
 * Project: user-center-realend
 * File: ErrorCode.java
 * Last date: 2023/11/18 下午5:36
 * Developer: KingYen
 *
 * Created by KingYen on 2023/11/18 17:36:46.
 */

package com.winter_wonder_house.user_center.common;

import lombok.Getter;

/**
 * ErrorCode
 * <p>
 * 0 - success
 * other - failure
 */
@Getter
public enum ErrorCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    // 相应状态码 0 - success, other - failure
    private final int code;

    // 相应状态信息 ok - success, other - failure
    private final String message;

    // 描述
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
