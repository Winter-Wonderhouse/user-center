package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.model.DTO.UserDTO;
import com.example.demo.model.domain.User;
import com.example.demo.model.request.UserLoginRequest;
import com.example.demo.model.request.UserRegisterRequest;
import com.example.demo.model.request.UserSearchRequest;
import com.example.demo.model.request.UserUpdateRequest;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenruohan
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2024-04-01 15:36:43
 */
@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, User>
        implements UserService {

    // Inject data operations.
    @Resource
    private UsersMapper usersMapper;

    /**
     * 用户注册业务逻辑
     *
     * @param userRegisterRequest
     * @return userDTO
     * @author KingYen.
     */
    @Override
    public UserDTO register(UserRegisterRequest userRegisterRequest) {
        // Whether the user is registered or not.
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userRegisterRequest.getUsername());
        long count = count(queryWrapper);

        // User is registered, return the error.
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username is already exist");
        }

        // User is not register, save the user info
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        // User password encryption
        String entryPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        user.setPassword(entryPassword);

        // Save the user base info
        boolean result = save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "register failed");
        }

        // return safety user info
        UserDTO userDTO = new UserDTO();
        userDTO.getUserDTO(user);
        return userDTO;

    }

    /**
     * 用户登陆逻辑
     *
     * @param userLoginRequest
     * @return userDTO
     * @author KingYen.
     */
    @Override
    public UserDTO login(UserLoginRequest userLoginRequest) {
        // User password encryption
        String encryptionPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        // Whether the user is registered or not.
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginRequest.getUsername());
        queryWrapper.eq("password", encryptionPassword);
        User user = getOne(queryWrapper);
        // User not registered, return the error
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username or password is wrong");
        }

        // The user verification passes, return safety user.
        UserDTO userDTO = new UserDTO();
        userDTO.getUserDTO(user);

        return userDTO;
    }

    /**
     * 更新用户信息业务逻辑
     *
     * @param userUpdateRequest
     * @param userInfo
     * @return userDTO
     * @author KingYen.
     */
    @Override
    public UserDTO update(UserUpdateRequest userUpdateRequest, UserDTO userInfo) {
        // Save or update the user info.
        User user = new User();
        user.setId(userInfo.getId());
        user.setUsername(userUpdateRequest.getUsername());
        user.setEmail(userUpdateRequest.getEmail());
        user.setPhone(userUpdateRequest.getPhone());
        boolean result = saveOrUpdate(user);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "update failed");
        }

        // Update success, return user safety info.
        userInfo.getUserDTO(user);

        return userInfo;
    }

    /**
     * 查询用户业务逻辑
     *
     * @param userSearchRequest
     * @return List<UserDTO>
     * @author KingYen.
     */
    @Override
    public List<UserDTO> getUserList(UserSearchRequest userSearchRequest) {
        Page<User> userPage = getUserPage(userSearchRequest);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!userSearchRequest.getType().isEmpty() || !userSearchRequest.getValue().isEmpty()) {
            // Search user info by Type and Value.
            queryWrapper.like(userSearchRequest.getType(), userSearchRequest.getValue());
        }

        List<User> userList = usersMapper.selectPage(userPage, queryWrapper).getRecords();

        // return user safety user info.
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User u : userList) {
            UserDTO userDTO = new UserDTO();
            userDTO.getUserDTO(u);
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    /**
     * 获取分页信息
     *
     * @param userSearchRequest
     * @return Page<>(page, size)
     * @author KingYen.
     */
    private static Page<User> getUserPage(UserSearchRequest userSearchRequest) {
        int page = userSearchRequest.getPage();
        if (page < 1) {
            page = 1;
        }
        int size = userSearchRequest.getSize();
        if (size < 10) {
            size = 30;
        }
        return new Page<>(page, size);
    }
}




