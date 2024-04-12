package com.example.demo.model.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String username;

    private String email;

    private String phone;
}
