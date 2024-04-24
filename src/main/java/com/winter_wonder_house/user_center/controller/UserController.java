package com.winter_wonder_house.user_center.controller;

import com.winter_wonder_house.user_center.common.BaseResponse;
import com.winter_wonder_house.user_center.model.DTO.UserDTO;
import com.winter_wonder_house.user_center.model.request.UserLoginRequest;
import com.winter_wonder_house.user_center.model.request.UserRegisterRequest;
import com.winter_wonder_house.user_center.model.request.UserSearchRequest;
import com.winter_wonder_house.user_center.model.request.UserUpdateRequest;
import com.winter_wonder_house.user_center.service.UserService;
import com.winter_wonder_house.user_center.utils.AuthUtils;
import com.winter_wonder_house.user_center.utils.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController 路由
 *
 * @author KingYen.
 */
@RestController
@RequestMapping("/user")
@Tag(name = "user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return BaseResponse<UserDTO>
     * @author KingYen.
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<UserDTO> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        userRegisterRequest.check();

        UserDTO result = userService.register(userRegisterRequest);

        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return BaseResponse<UserDTO>
     * @author KingYen.
     */
    @Operation(summary = "用户登陆")
    @PostMapping("/login")
    public BaseResponse<UserDTO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        userLoginRequest.check();

        UserDTO result = userService.login(userLoginRequest);

        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userService.getUserList(userSearchRequest, result.getId());

        // Set session to mark the user as logged in.
        request.getSession().setAttribute("user", result);

        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return BaseResponse<UserDTO>
     * @author KingYen.
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/current")
    public BaseResponse<UserDTO> currentUser(HttpServletRequest request) {
        return ResultUtils.success(AuthUtils.isLogin(request));
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/update")
    public BaseResponse<UserDTO> update(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        UserDTO userInfo = AuthUtils.isLogin(request);
        return ResultUtils.success(userService.update(userUpdateRequest, userInfo));
    }

    /**
     * 用户登出
     *
     * @param request
     * @return BaseResponse<String>
     * @author KingYen.
     */
    @Operation(summary = "用户登出")
    @GetMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        request.removeAttribute("user");

        return ResultUtils.success("logout success");
    }

    /**
     * 搜索用户列表
     *
     * @param userSearchRequest
     * @param request
     * @return BaseResponse<List<UserDTO>>
     * @author KingYen.
     */
    @Operation(summary = "搜索用户")
    @PostMapping("/search")
    public BaseResponse<List<UserDTO>> searchUserList(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        // Check the user as logging in and is admin.
        UserDTO userInfo = AuthUtils.isRole(request);

        List<UserDTO> userDTOList = userService.getUserList(userSearchRequest, userInfo.getId());
        return ResultUtils.success(userDTOList);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return BaseResponse<Boolean>
     * @author KingYen.
     */
    @Operation(summary = "删除用户")
    @GetMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        // Check the user as logging in and is admin.
        AuthUtils.isRole(request);

        return ResultUtils.success(userService.delete(id));
    }
}


