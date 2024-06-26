package com.winter_wonder_house.user_center.exception;

import com.winter_wonder_house.user_center.common.BaseResponse;
import com.winter_wonder_house.user_center.common.ErrorCode;
import com.winter_wonder_house.user_center.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author KingYen.
 * @from QIT Software Studio
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: {}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}