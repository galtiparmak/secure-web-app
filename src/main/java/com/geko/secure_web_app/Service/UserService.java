package com.geko.secure_web_app.Service;

import com.geko.secure_web_app.DTO.UserDTO;

public interface UserService {
    UserDTO getMyInformation(String username);
}
