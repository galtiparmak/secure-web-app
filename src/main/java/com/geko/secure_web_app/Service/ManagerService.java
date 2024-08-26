package com.geko.secure_web_app.Service;

import com.geko.secure_web_app.DTO.UserDTO;

import java.util.List;

public interface ManagerService {
    List<UserDTO> getAllUsersExceptAdmins();
    UserDTO getUserInformation(String username);
}