package com.geko.secure_web_app.ControllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.geko.secure_web_app.Controller.AdminController;
import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Service.AdminService;
import com.geko.secure_web_app.Service.Impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImpl adminServiceImpl;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void assignRoleTest_ShouldAssignRole() throws Exception {
        // Arrange
        String username = "testuser";
        Role role = Role.ADMIN;

        // Act & Assert
        mockMvc.perform(post("/assign-role")
                        .param("username", username)
                        .param("role", role.name())
                        .with(user("admin").roles("ADMIN"))  // Mock authentication as an admin
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User role updated successfully"));

        // Verify that the service method was called with the correct parameters
        verify(adminServiceImpl).assignRole(username, role);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void assignRoleTest_ShouldReturnForbiddenForNonAdmin() throws Exception {
        // Arrange
        String username = "testuser";
        Role role = Role.ADMIN;

        // Act & Assert
        mockMvc.perform(post("/assign-role")
                        .param("username", username)
                        .param("role", role.name())
                        .with(user("user").roles("USER"))  // Mock authentication as a non-admin
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Verify that the service method was not called since the user is not an admin
        verify(adminServiceImpl, never()).assignRole(anyString(), any(Role.class));
    }

}
