package com.example.demo.model.request;

import lombok.Getter;

@Getter
public class UserSearchRequest {
    private String type;
    private String value;
    private int page;
    private int size;
}
