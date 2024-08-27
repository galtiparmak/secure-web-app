package com.geko.secure_web_app.ServiceTest;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.Impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsersAsDTOs() {
        User user1 = new User(1L, "user1", "password", "user1@example.com", Role.USER);
        User user2 = new User(2L, "user2", "password", "user2@example.com", Role.MANAGER);
        User user3 = new User(3L, "admin1", "password", "admin1@example.com", Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2, user3));

        List<UserDTO> userDTOs = adminService.getAllUsers();

        assertEquals(3, userDTOs.size());
        assertEquals("user1", userDTOs.get(0).getUsername());
        assertEquals("user2", userDTOs.get(1).getUsername());
        assertEquals("admin1", userDTOs.get(2).getUsername());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void assignRole_ShouldUpdateUserRole() {
        User user = new User(1L, "user1", "password", "user1@example.com", Role.USER);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        adminService.assignRole("user1", Role.MANAGER);
        assertEquals(Role.MANAGER, user.getRole());

        // Verify repository interactions
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void assignRole_ShouldThrowExceptionIfUserNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> adminService.assignRole("user1", Role.MANAGER));

        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, never()).save(any());
    }
}
