package com.winter_wonder_house.user_center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winter_wonder_house.user_center.model.DTO.UserDTO;
import com.winter_wonder_house.user_center.model.domain.User;
import com.winter_wonder_house.user_center.model.request.UserLoginRequest;
import com.winter_wonder_house.user_center.model.request.UserRegisterRequest;
import com.winter_wonder_house.user_center.model.request.UserSearchRequest;
import com.winter_wonder_house.user_center.model.request.UserUpdateRequest;

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

    boolean delete(Long id);

    List<UserDTO> getUserList(UserSearchRequest userSearchRequest, Long userID);

}
