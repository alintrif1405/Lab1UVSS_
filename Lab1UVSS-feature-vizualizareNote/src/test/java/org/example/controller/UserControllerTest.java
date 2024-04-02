package org.example.controller;

import org.example.model.ERole;
import org.example.model.User;
import org.example.service.EmailService;
import org.example.service.AccountHistoryService;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private AccountHistoryService accountHistoryService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    private MockMvc mockMvc;

    @Test
    void saveUserValid() throws Exception {
        User validUserToSave = new User(1, "Radu", "Valentin", "user2@example.com", "Yth67#890afa", ERole.STD);

        doReturn(validUserToSave).when(userService).saveUser(any(User.class));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(post("/users/register")
                        .param("lastname", validUserToSave.getLastname())
                        .param("firstname", validUserToSave.getFirstname())
                        .param("email", validUserToSave.getEmail())
                        .param("password", validUserToSave.getPassword())
                        .param("role", validUserToSave.getRole().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully created"))
                .andReturn().getResponse().getContentAsString();
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void saveUserInvalid() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(post("/users/register")
                        .param("lastname", "Radu")
                        .param("firstname", "Valentin")
                        .param("email", "user1@example.com")
                        .param("password", "12345")
                        .param("role", "STD"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid user"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void saveUser_ValidUser_ReturnsSavedUser() {
        // Mocking
        User user = new User(); // Create a valid user
        when(userService.saveUser(user)).thenReturn(user);

        // Test
        ResponseEntity<User> response = userController.saveUser(user);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void getUser_ValidEmail_ReturnsUser() {
        // Mocking
        String email = "test@example.com"; // Valid email
        User user = new User(); // Create a user
        when(userService.getUserByEmail(email)).thenReturn(user);

        // Test
        ResponseEntity<User> response = userController.getUser(email);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void updateUser_ValidUser_ReturnsOK() {
        // Mocking
        User user = new User();
        user.setLastname("Doe");
        user.setFirstname("John");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(ERole.STD); // Set a role for the user

        when(userService.updateUser(any(User.class))).thenReturn(user);

        // Test
        ResponseEntity<User> response = userController.updateUser(user.getEmail(), user.getPassword());

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).updateUser(any(User.class));
    }
}
