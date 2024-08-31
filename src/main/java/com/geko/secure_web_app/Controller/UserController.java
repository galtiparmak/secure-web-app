package com.geko.secure_web_app.Controller;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInformation(Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userService.getMyInformation(username);
        return ResponseEntity.ok(userDTO);
    } // http://localhost:8080/api/user/me

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam String email) {
        return ResponseEntity.ok(userService.resetPasswordRequest(email));
    } // http://localhost:8080/api/user/reset-password-request?email=...

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        return ResponseEntity.ok(userService.resetPassword(token, password));
    } // http://localhost:8080/api/user/reset-password?token=...&password=...
}

