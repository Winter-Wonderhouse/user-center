package com.example.demo.exception;

import com.example.demo.common.ErrorCode;

/**
 * 异常处理
 *
 * @author KingYen.
 * @from QIT Software Studio
 */
public class BusinessException extends RuntimeException {

    /**
     * code
     * 相应状态码
     */
    private final int code;

    /**
     * description
     * 描述
     */
    private final String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String exception) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = exception;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
