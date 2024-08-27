package com.geko.secure_web_app.ServiceTest;

import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import com.geko.secure_web_app.Service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        // Arrange
        User user = new User(1L, "testUser", "password", "test@example.com", Role.USER);
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertThat(token).isNotNull();
        assertThat(jwtService.extractUserName(token)).isEqualTo(user.getUsername());
    }

    @Test
    void testExtractUserName() throws Exception {
        // Arrange
        Method getSignInKeyMethod = JwtService.class.getDeclaredMethod("getSignInKey");
        getSignInKeyMethod.setAccessible(true);
        Key signInKey = (Key) getSignInKeyMethod.invoke(jwtService);

        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();

        // Act
        String username = jwtService.extractUserName(token);

        // Assert
        assertThat(username).isEqualTo("testUser");
    }

    @Test
    void testIsTokenValid() {
        // Arrange
        User user = new User(1L, "testUser", "password", "test@example.com", Role.USER);
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() throws Exception {
        Method getSignInKeyMethod = JwtService.class.getDeclaredMethod("getSignInKey");
        getSignInKeyMethod.setAccessible(true);
        Key signInKey = (Key) getSignInKeyMethod.invoke(jwtService);



        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 10)) // 10 days ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // Expired 1 day ago
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();

        Method isExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isExpiredMethod.setAccessible(true);
        boolean isExpired = (boolean) isExpiredMethod.invoke(jwtService);

        assertTrue(isExpired);
    }
}
