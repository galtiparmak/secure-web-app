package com.geko.secure_web_app.Controller;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam Role role) {
        adminService.assignRole(username, role);
        return ResponseEntity.ok("User role updated successfully");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}

