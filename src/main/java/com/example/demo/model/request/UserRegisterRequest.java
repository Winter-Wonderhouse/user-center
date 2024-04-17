package com.example.demo.model.request;

import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserRegisterRequest {

    private String username;

    private String password;

    private String checkPassword;

    public void check() {
        if (this.username.isEmpty() || this.password.isEmpty() || this.checkPassword.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (this.username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is too long");
        }

        if (this.password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password must be at least 8 characters");
        }

        if (!this.password.equals(this.checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "passwords are not the same");
        }
    }
}
