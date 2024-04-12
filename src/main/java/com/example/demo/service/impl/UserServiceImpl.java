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
import jakarta.servlet.http.HttpServletRequest;
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
     * @param request
     * @return userDTO
     */
    @Override
    public UserDTO login(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // User password encryption
        String encrypthonPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        // Whether the user is registered or not.
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginRequest.getUsername());
        queryWrapper.eq("password", encrypthonPassword);
        User user = getOne(queryWrapper);
        // User not registered, return the error
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username or password is wrong");
        }

        // The user verification passes, return safety user.
        UserDTO userDTO = new UserDTO();
        userDTO.getUserDTO(user);

        // Set session to mark the user as logged in.
        request.getSession().setAttribute("user", userDTO);
        return userDTO;
    }

    /**
     * 获取用户基本信息
     *
     * @param request
     * @return userDTO
     */
    @Override
    public UserDTO currentUser(HttpServletRequest request) {
        // Check the user as logged in by user session
        Object sessionObj = request.getSession().getAttribute("user");
        UserDTO user = (UserDTO) sessionObj;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // Get user safety info
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        User currentUser = getOne(queryWrapper);
        UserDTO userDTO = new UserDTO();
        userDTO.getUserDTO(currentUser);
        return userDTO;
    }

    /**
     * 更新用户信息业务逻辑
     *
     * @param userUpdateRequest
     * @param request
     * @return userDTO
     */
    @Override
    public UserDTO update(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // Check the user as logging in and get user info.
        Object sessionInfo = request.getSession().getAttribute("user");
        UserDTO userInfo = (UserDTO) sessionInfo;
        if (userInfo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

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
     * 用户登出逻辑
     *
     * @param request
     * @return boolean
     * @author KingYen.
     */
    @Override
    public boolean logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return true;
    }

    /**
     * 查询用户业务逻辑
     *
     * @param userSearchRequest
     * @param request
     * @return List<UserDTO>
     * @author KingYen.
     */
    @Override
    public List<UserDTO> getUserList(UserSearchRequest userSearchRequest, HttpServletRequest request) {
        // Check the user as logging in and get user info.
        Object sessionObj = request.getSession().getAttribute("user");
        Page<User> userPage = getUserPage(userSearchRequest, (UserDTO) sessionObj);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // TODO if type and value is empty get all user list.
        if (userSearchRequest.getType().isEmpty() || userSearchRequest.getValue().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // Seach user info by Type and Value.
        queryWrapper.like(userSearchRequest.getType(), userSearchRequest.getValue());
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

    /**
     * 获取分页信息
     *
     * @param userSearchRequest
     * @param sessionObj
     * @return Page<>(page, size)
     * @author KingYen.
     */
    private static Page<User> getUserPage(UserSearchRequest userSearchRequest, UserDTO sessionObj) {
        // TODO Split permission verification.
        if (sessionObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        if (sessionObj.getRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

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




