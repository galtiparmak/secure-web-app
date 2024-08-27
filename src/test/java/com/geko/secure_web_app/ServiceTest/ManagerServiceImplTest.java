package com.geko.secure_web_app.ServiceTest;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.Impl.ManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ManagerServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ManagerServiceImpl managerServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsersExceptAdmins_ShouldReturnAllUsersAsDTOsExceptAdmins() {
        User user1 = new User(1L, "user1", "password", "user1@example.com", Role.USER);
        User user2 = new User(2L, "user2", "password", "user2@example.com", Role.MANAGER);
        User user3 = new User(3L, "admin1", "password", "admin1@example.com", Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2, user3));

        List<UserDTO> userDTOs = managerServiceImpl.getAllUsersExceptAdmins();

        assertEquals(2, userDTOs.size());
        assertEquals("user1", userDTOs.get(0).getUsername());
        assertEquals("user2", userDTOs.get(1).getUsername());

        verify(userRepository,times(1)).findAll();
    }

    @Test
    void getUserInformation_ShouldReturnUserInformation() {
        User user = new User(1L, "user1", "password", "user1@example.com", Role.USER);
        when(userRepository.findByUsername("user1")).thenReturn(java.util.Optional.of(user));
        UserDTO userDTO = managerServiceImpl.getUserInformation("user1");
        assertEquals("user1", userDTO.getUsername());
        verify(userRepository, times(1)).findByUsername("user1");
    }
}
