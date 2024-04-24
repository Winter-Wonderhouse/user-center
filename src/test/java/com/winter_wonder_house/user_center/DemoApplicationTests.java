package com.winter_wonder_house.user_center;

import com.winter_wonder_house.user_center.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    void contextLoads() {
        List<User> userList = new ArrayList<User>();
        User user = new User();
        userList.add(user);
        redisTemplate.opsForValue().set("test", userList);
    }

}
