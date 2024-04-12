package com.example.demo.model.DTO;

import com.example.demo.model.domain.User;
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
