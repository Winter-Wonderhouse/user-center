package com.example.demo.utils;

import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {
    public static UserDTO isLogin(HttpServletRequest request) {
        return (UserDTO) request.getSession().getAttribute("user");
    }

    public static boolean isRole(HttpServletRequest request) {
        UserDTO userObj = isLogin(request);

        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        return userObj.getRole() == 1;
    }
}
