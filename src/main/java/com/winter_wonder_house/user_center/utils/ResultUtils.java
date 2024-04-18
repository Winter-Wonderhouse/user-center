/*
 * Copyright (c) 2023.
 * Project: user-center-realend
 * File: ResultUtils.java
 * Last date: 2023/11/18 下午5:36
 * Developer: KingYen
 *
 * Created by KingYen on 2023/11/18 17:36:46.
 */

package com.winter_wonder_house.user_center.utils;

import com.winter_wonder_house.user_center.common.BaseResponse;
import com.winter_wonder_house.user_center.common.ErrorCode;
import lombok.Data;

@Data
public class ResultUtils {
    /**
     * Success
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse(0, "ok", data, "success");
    }

    /**
     * Failures
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * Failures
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, message, null, description);
    }

    /**
     * Failures
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), message, null, description);
    }

    /**
     * Failures
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
    }

}