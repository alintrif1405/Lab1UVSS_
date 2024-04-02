package org.example.service;

import org.example.model.ERole;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void saveUserValid(){
        List<User> users = List.of(
                new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD)
        );
        when(userRepository.findAll()).thenReturn(users);

        User validUserToSave = new User(2, "Radu", "Valentin", "user2@example.com", "Yth67#890afa", ERole.STD);
        assertTrue(userService.validateEmail(validUserToSave.getEmail()));
        assertTrue(userService.validatePassword(validUserToSave.getPassword()));
        assertTrue(userService.validateNames(validUserToSave.getFirstname()));
        assertTrue(userService.validateNames(validUserToSave.getLastname()));

        when(userRepository.save(validUserToSave)).thenReturn(validUserToSave);
        User savedUser = userService.saveUser(validUserToSave);

        assertNotNull(savedUser);
        verify(userRepository,times(1)).save(validUserToSave);
    }

    @Test
    void saveUserInvalid(){
        User invalidUserToSave = new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD);
        assertFalse(userService.validatePassword(invalidUserToSave.getPassword()));

        User savedUser = userService.saveUser(invalidUserToSave);
        verify(userRepository, never()).save(any());
        assertNull(savedUser);
    }

    @Test
    void validateEmail() {
        List<User> users = Arrays.asList(
                new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD),
                new User(2, "Mihai", "Augustin", "user2@example.com", "12345", ERole.ADM)
        );
        when(userRepository.findAll()).thenReturn(users);

        assertFalse(userService.validateEmail(null));
        assertTrue(userService.validateEmail("donationmanager@yahoo.com"));
        assertFalse(userService.validateEmail("_@_"));
        assertFalse(userService.validateEmail("boss_tudor@"));
        assertFalse(userService.validateEmail("boss_tudor@ya"));
        assertTrue(userService.validateEmail("boss_tudor@yahoo.com"));
        assertFalse(userService.validateEmail("boss_tudor@yahoo."));
        assertFalse(userService.validateEmail("user1@example.com"));

        verify(userRepository, times(7)).findAll();

    }

    @Test
    void validateNames() {
        assertFalse(userService.validateNames(null));
        assertTrue(userService.validateNames("Radu"));
        assertFalse(userService.validateNames("123iwe"));
        assertFalse(userService.validateNames("ra du"));
        assertFalse(userService.validateNames("S"));
    }

    @Test
    void validatePassword() {
        assertFalse(userService.validatePassword(null));
        assertFalse(userService.validatePassword("1234567"));
        assertFalse(userService.validatePassword("abafvahjfvwefwef"));
        assertFalse(userService.validatePassword("Rbuikafafwe"));
        assertFalse(userService.validatePassword("        "));
        assertFalse(userService.validatePassword("1Aa@"));
        assertFalse(userService.validatePassword("Rhvjaj12"));
        assertTrue(userService.validatePassword("Rdi%12fjkw"));
    }

    @Test
    void saveUser_ValidUser_ReturnsSavedUser() {
        // Mocking
        User user = new User(1, "John", "Doe", "john@example.com", "Pass@123", null);
        when(userRepository.save(any(User.class))).thenReturn(user); // Stubbing the save method to return the user

        User savedUser = userService.saveUser(user);

        // Assertions and verifications
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class)); // Verifying userRepository.save() is called once with any User object
    }


    @Test
    void saveUser_EncryptsPasswordAndValidates() {
        // Mocking
        User user = new User(1, "John", "Doe", "john@example.com", "Pass@123", null);
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setPassword(encodedPassword);
            return userToSave;
        });

        // Test
        User savedUser = userService.saveUser(user);

        // Assertion
        assertNotNull(savedUser);
        assertEquals(encodedPassword, savedUser.getPassword());
        assertTrue(userService.validateEmail(user.getEmail()));
        assertTrue(userService.validateNames(user.getFirstname()));
        assertTrue(userService.validateNames(user.getLastname()));
        assertTrue(userService.validatePassword(user.getPassword())); // Assuming the password meets criteria
        Mockito.verify(userRepository).save(user);
    }



    @Test
    void saveUser_InvalidUser_ReturnsNull() {
        // Mocking
        User user = new User(); // Invalid user without required fields

        // Test
        User savedUser = userService.saveUser(user);

        // Assertion
        assertNull(savedUser);
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateUser_UserNotFound_ReturnsNull() {
        // Creating a user
        User user = new User(1, "John", "Doe", "john@example.com", "Pass@123", null);

        // Stubbing the findByEmail method to return null
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Test
        User updatedUser = userService.updateUser(user);

        // Assertion
        assertNull(updatedUser);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class)); // Ensure save method is not called
    }


    @Test
    void getUserByEmail_ExistingEmail_ReturnsUser() {
        // Mocking
        String email = "john@example.com";
        User user = new User(1, "John", "Doe", email, "Pass@123", null);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Test
        User foundUser = userService.getUserByEmail(email);

        // Assertion
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserByEmail_NonExistingEmail_ReturnsNull() {
        // Mocking
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Test
        User foundUser = userService.getUserByEmail(email);

        // Assertion
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void validateEmail_ValidEmail_ReturnsTrue() {
        // Test
        boolean isValid = userService.validateEmail("john@example.com");

        // Assertion
        assertTrue(isValid);
    }

    @Test
    void validateEmail_InvalidEmail_ReturnsFalse() {
        // Test
        boolean isValid = userService.validateEmail("invalidemail");

        // Assertion
        assertFalse(isValid);
    }

    @Test
    void validatePassword_ValidPassword_ReturnsTrue() {
        // Test
        boolean isValid = userService.validatePassword("Pass@123");

        // Assertion
        assertTrue(isValid);
    }

    @Test
    void validatePassword_InvalidPassword_ReturnsFalse() {
        // Test
        boolean isValid = userService.validatePassword("invalidpassword");

        // Assertion
        assertFalse(isValid);
    }

    @Test
    void validateNames_ValidName_ReturnsTrue() {
        // Test
        boolean isValid = userService.validateNames("John");

        // Assertion
        assertTrue(isValid);
    }

    @Test
    void validateNames_InvalidName_ReturnsFalse() {
        // Test
        boolean isValid = userService.validateNames("123");

        // Assertion
        assertFalse(isValid);
    }

    @Test
    void validateEmailOnUpdate_ValidEmail_ReturnsTrue() {
        // Test
        boolean isValid = userService.validateEmailOnUpdate("john@example.com");

        // Assertion
        assertTrue(isValid);
    }

    @Test
    void validateEmailOnUpdate_InvalidEmail_ReturnsFalse() {
        // Test
        boolean isValid = userService.validateEmailOnUpdate("invalidemail");

        // Assertion
        assertFalse(isValid);
    }

}
