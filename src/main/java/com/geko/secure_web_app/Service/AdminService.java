package com.geko.secure_web_app.Service;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;

import java.util.List;

public interface AdminService {
    List<UserDTO> getAllUsers();
    void assignRole(String username, Role role);
}
