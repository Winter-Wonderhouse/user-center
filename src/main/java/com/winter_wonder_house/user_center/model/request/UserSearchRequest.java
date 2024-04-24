package com.winter_wonder_house.user_center.model.request;

import lombok.Getter;

@Getter
public class UserSearchRequest {

    private String type;

    private String value;

    private int current;

    private int size;

}
