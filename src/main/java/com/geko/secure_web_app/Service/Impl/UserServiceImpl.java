package com.geko.secure_web_app.Service.Impl;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.PasswordResetToken;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.PasswordResetTokenRepository;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO getMyInformation(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user, calculateExpiryDate(24));
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public Date calculateExpiryDate(int expiryTimeInHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.HOUR, expiryTimeInHours);
        return new Date(calendar.getTime().getTime());
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        String subject = "Password Reset Request";
        String resetUrl = "http://localhost:8080/api/user/reset-password?token=" + resetToken;
        String message = "To reset your password, click the link below:\n" + resetUrl;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setFrom("*****");
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }

    public String resetPasswordRequest(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        sendPasswordResetEmail(email, token);

        return "Email sent";
    }

    public String resetPassword(String token, String password) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return "Invalid token";
        }

        PasswordResetToken myToken = optionalToken.get();

        User user = myToken.getUser();
        Date date = myToken.getExpiryDate();

        if (date.before(new Date())) {
            return "Token expired";
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetTokenRepository.delete(myToken);

        return "Password reset";
    }
}
