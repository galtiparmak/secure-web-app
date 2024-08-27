package com.geko.secure_web_app.ServiceTest;

import com.geko.secure_web_app.DTO.UserDTO;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMyInformation_ShouldReturnUserInformation() {
        User user = new User(1L, "user1", "password", "user1@example.com", Role.USER);
        when(userRepository.findByUsername("user1")).thenReturn(java.util.Optional.of(user));
        UserDTO userDTO = userServiceImpl.getMyInformation("user1");
        assertEquals("user1", userDTO.getUsername());
        verify(userRepository, times(1)).findByUsername("user1");
    }
}
