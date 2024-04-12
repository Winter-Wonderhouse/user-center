package com.example.demo.model.request;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String username;
    private String password;
    private String checkPassword;
}
