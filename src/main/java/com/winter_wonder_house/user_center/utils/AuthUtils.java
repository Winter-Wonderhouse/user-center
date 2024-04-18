package com.winter_wonder_house.user_center.utils;

import com.winter_wonder_house.user_center.common.ErrorCode;
import com.winter_wonder_house.user_center.exception.BusinessException;
import com.winter_wonder_house.user_center.model.DTO.UserDTO;
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
