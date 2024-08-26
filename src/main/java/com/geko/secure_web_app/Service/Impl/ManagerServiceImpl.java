package com.geko.secure_web_app.Service.Impl;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;

    @Autowired
    public ManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsersExceptAdmins() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        users.stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .forEach(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());
                    userDTOs.add(dto);
                });

        return userDTOs;
    }

    @Override
    public UserDTO getUserInformation(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = optionalUser.get();

        if (user.getRole() == Role.ADMIN) {
            throw new AccessDeniedException("Managers cannot view admin information");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());

        return userDTO;
    }
}
