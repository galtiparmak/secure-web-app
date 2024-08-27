package com.geko.secure_web_app.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.geko.secure_web_app.Auth.AuthenticationRequest;
import com.geko.secure_web_app.Auth.AuthenticationResponse;
import com.geko.secure_web_app.Auth.RegisterRequest;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Repository.UserRepository;
import com.geko.secure_web_app.Service.AuthenticationService;
import com.geko.secure_web_app.Service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_ShouldReturnAuthenticationResponse() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password");

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticate_ShouldReturnAuthenticationResponse() {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testuser");
        authenticationRequest.setPassword("password");

        User user = User.builder()
                .username(authenticationRequest.getUsername())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(authenticationRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticate_ShouldThrowUsernameNotFoundException() {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testuser");
        authenticationRequest.setPassword("password");

        when(userRepository.findByUsername(authenticationRequest.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(authenticationRequest));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }
}
