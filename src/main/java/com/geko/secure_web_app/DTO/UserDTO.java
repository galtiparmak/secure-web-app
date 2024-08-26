package com.geko.secure_web_app.DTO;

import com.geko.secure_web_app.Entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}

