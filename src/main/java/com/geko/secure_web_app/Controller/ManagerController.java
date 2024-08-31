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

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersExceptAdmins() {
        List<UserDTO> users = managerService.getAllUsersExceptAdmins();
        return ResponseEntity.ok(users);
    } // http://localhost:8080/api/manager/users

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDTO> getUserInformation(@PathVariable String username) {
        UserDTO userDTO = managerService.getUserInformation(username);
        return ResponseEntity.ok(userDTO);
    } // http://localhost:8080/api/manager/user/testuser
}

