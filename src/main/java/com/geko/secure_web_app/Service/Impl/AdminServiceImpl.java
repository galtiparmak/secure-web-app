package com.geko.secure_web_app.Service.Impl;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void assignRole(String username, Role role) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        users.forEach(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            userDTOs.add(dto);
        });

        return userDTOs;
    }
}
