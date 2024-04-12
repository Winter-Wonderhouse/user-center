package com.example.demo.controller;

import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.DTO.UserDTO;
import com.example.demo.model.request.UserLoginRequest;
import com.example.demo.model.request.UserRegisterRequest;
import com.example.demo.model.request.UserSearchRequest;
import com.example.demo.model.request.UserUpdateRequest;
import com.example.demo.service.UserService;
import com.example.demo.utils.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController 路由
 *
 * @author wenruohan
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return BaseResponse<UserDTO>
     * @author wenruohan
     */
    @PostMapping("/register")
    public BaseResponse<UserDTO> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest.getUsername().isEmpty() || userRegisterRequest.getPassword().isEmpty() || userRegisterRequest.getCheckPassword().isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (userRegisterRequest.getUsername().length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is too long");
        }

        if (userRegisterRequest.getPassword().length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password must be at least 8 characters");
        }

        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "passwords are not the same");
        }

        UserDTO result = userService.register(userRegisterRequest);

        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return BaseResponse<UserDTO>
     * @author wenruohan
     */
    @PostMapping("/login")
    public BaseResponse<UserDTO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest.getUsername().isEmpty() || userLoginRequest.getPassword().isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (userLoginRequest.getUsername().length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is too long");
        }

        if (userLoginRequest.getPassword().length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "password must be at least 8 characters");
        }

        UserDTO result = userService.login(userLoginRequest, request);

        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return BaseResponse<UserDTO>
     * @author wenruohan
     */
    @GetMapping("/current")
    public BaseResponse<UserDTO> currentUser(HttpServletRequest request) {
        UserDTO result = userService.currentUser(request);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<UserDTO> update(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        UserDTO result = userService.update(userUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 用户登出
     *
     * @param request
     * @return BaseResponse<String>
     * @author wenruohan
     */
    @GetMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        if (!userService.logout(request)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "logout failed");
        }

        return ResultUtils.success("logout success");
    }

    /**
     * 搜索用户列表
     *
     * @param userSearchRequest
     * @param request
     * @return BaseResponse<List<UserDTO>>
     */
    @PostMapping("/search")
    public BaseResponse<List<UserDTO>> searchUserList(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        List<UserDTO> result = userService.getUserList(userSearchRequest, request);
        return ResultUtils.success(result);
    }
}


