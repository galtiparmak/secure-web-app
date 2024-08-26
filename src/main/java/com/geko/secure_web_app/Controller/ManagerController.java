package com.geko.secure_web_app.Controller;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Service.AuthenticationService;
import com.geko.secure_web_app.Service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService, AuthenticationService authenticationService) {
        this.managerService = managerService;
    }

    // Endpoint for the manager to view all users except admins
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersExceptAdmins() {
        List<UserDTO> users = managerService.getAllUsersExceptAdmins();
        return ResponseEntity.ok(users);
    }

    // Endpoint for a manager to view a specific user's information by username
    @GetMapping("/user/{username}")
    public ResponseEntity<UserDTO> getUserInformation(@PathVariable String username) {
        UserDTO userDTO = managerService.getUserInformation(username);
        return ResponseEntity.ok(userDTO);
    }
}

