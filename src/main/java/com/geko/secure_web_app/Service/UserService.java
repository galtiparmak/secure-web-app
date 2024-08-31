package com.geko.secure_web_app.Service;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.User;

import java.util.Date;

public interface UserService {
    UserDTO getMyInformation(String username);
    void createPasswordResetTokenForUser(User user, String token);
    Date calculateExpiryDate(int expiryTimeInHours);
    void sendPasswordResetEmail(String email, String resetToken);
    String resetPasswordRequest(String email);
    String resetPassword(String token, String password);
}
