package com.example.demo.model.request;

import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserLoginRequest {

    private String username;

    private String password;

    public void check() {
        if (this.username.isEmpty() || this.password.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (this.username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is too long");
        }

        if (this.password.length() < 8 || this.password.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password must be at least 8 characters and larger than 20 characters");
        }
    }
}
