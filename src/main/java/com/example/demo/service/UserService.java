package com.example.demo.service;

import com.example.demo.model.DTO.UserDTO;
import com.example.demo.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.model.request.UserLoginRequest;
import com.example.demo.model.request.UserRegisterRequest;
import com.example.demo.model.request.UserSearchRequest;
import com.example.demo.model.request.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author wenruohan
* @description 针对表【users】的数据库操作Service
* @createDate 2024-04-01 15:36:43
*/
public interface UserService extends IService<User> {

    UserDTO register(UserRegisterRequest userRegisterRequest);

    UserDTO login(UserLoginRequest userLoginRequest, HttpServletRequest request);

    UserDTO currentUser(HttpServletRequest request);

    UserDTO update(UserUpdateRequest userDTO, HttpServletRequest request);

    boolean logout(HttpServletRequest request);

    List<UserDTO> getUserList(UserSearchRequest userSearchRequest, HttpServletRequest request);
}
