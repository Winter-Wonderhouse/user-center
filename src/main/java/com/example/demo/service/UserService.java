package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.model.DTO.UserDTO;
import com.example.demo.model.domain.User;
import com.example.demo.model.request.UserLoginRequest;
import com.example.demo.model.request.UserRegisterRequest;
import com.example.demo.model.request.UserSearchRequest;
import com.example.demo.model.request.UserUpdateRequest;

import java.util.List;

/**
 * @author wenruohan
 * @description 针对表【users】的数据库操作Service
 * @createDate 2024-04-01 15:36:43
 */
public interface UserService extends IService<User> {

    UserDTO register(UserRegisterRequest userRegisterRequest);

    UserDTO login(UserLoginRequest userLoginRequest);

    UserDTO update(UserUpdateRequest userDTO, UserDTO request);

    List<UserDTO> getUserList(UserSearchRequest userSearchRequest);

    boolean deleteUser(Long id);
}
