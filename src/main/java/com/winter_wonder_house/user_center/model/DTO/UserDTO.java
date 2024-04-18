package com.winter_wonder_house.user_center.model.DTO;

import com.winter_wonder_house.user_center.model.domain.User;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private Integer role;

    private String email;

    private String phone;

    public void getUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }
}
