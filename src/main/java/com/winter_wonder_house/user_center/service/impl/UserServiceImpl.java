package com.winter_wonder_house.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winter_wonder_house.user_center.common.ErrorCode;
import com.winter_wonder_house.user_center.exception.BusinessException;
import com.winter_wonder_house.user_center.mapper.UsersMapper;
import com.winter_wonder_house.user_center.model.DTO.UserDTO;
import com.winter_wonder_house.user_center.model.domain.User;
import com.winter_wonder_house.user_center.model.request.UserLoginRequest;
import com.winter_wonder_house.user_center.model.request.UserRegisterRequest;
import com.winter_wonder_house.user_center.model.request.UserSearchRequest;
import com.winter_wonder_house.user_center.model.request.UserUpdateRequest;
import com.winter_wonder_house.user_center.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wenruohan
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2024-04-01 15:36:43
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UsersMapper, User>
        implements UserService {

    // Inject data operations.
    @Resource
    private UsersMapper usersMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
        log.info("register user: {}", user);

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

        // Get user info
        User user = getOne(queryWrapper);

        // User not registered, return the error
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "username or password is wrong");
        }

        // The user verification passes, return safety user.
        UserDTO userDTO = new UserDTO();
        userDTO.getUserDTO(user);
        log.info("user login success: {}", userDTO);

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
        log.info("update user: {}", user);

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
    public List<UserDTO> getUserList(UserSearchRequest userSearchRequest, Long userID) {
        Page<User> userPage = getUserPage(userSearchRequest);
        log.info("current: {} size: {}", userPage.getCurrent(), userPage.getSize());
        String redisKey = String.format("user:center:list:%s:%s:%s", userID, userSearchRequest.getType(), userSearchRequest.getValue());
        log.info("redisKey: {}", redisKey);
        List<UserDTO> page = (List<UserDTO>) redisTemplate.opsForValue().get(redisKey);
        if (page != null) {
            log.info("get user list by redis: {}", page);
            return page;
        }
        QueryWrapper<User> queryWrapper = getQueryWrapper(userSearchRequest);;
        List<User> userList = usersMapper.selectPage(userPage, queryWrapper).getRecords();
        // return user safety user info.
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User u : userList) {
            UserDTO userDTO = new UserDTO();
            userDTO.getUserDTO(u);
            userDTOList.add(userDTO);
        }

        redisTemplate.opsForValue().set(redisKey, userDTOList, 1, TimeUnit.MINUTES);
        log.info("get user list by mysql: {}", userDTOList);

        return userDTOList;
    }

    private QueryWrapper<User> getQueryWrapper(UserSearchRequest userSearchRequest) {
        if (userSearchRequest.getType() != null && userSearchRequest.getValue() != null) {
            if (userSearchRequest.getType().isEmpty() || userSearchRequest.getValue().isEmpty()) {
                return null;
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            return queryWrapper.like(userSearchRequest.getType(), userSearchRequest.getValue());
        }

        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("delete user: {}", id);
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
        int current = userSearchRequest.getCurrent();
        if (current < 1) {
            current = 1;
        }
        int size = userSearchRequest.getSize();
        if (size < 10) {
            size = 30;
        }
        return new Page<>(current, size);
    }
}




