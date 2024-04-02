package org.example.controller;

import org.example.model.ERole;
import org.example.model.User;
import org.example.service.EmailService;
import org.example.service.AccountHistoryService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final AccountHistoryService accountHistoryService;

    @Autowired
    public UserController(UserService userService, AccountHistoryService accountHistoryService, EmailService emailService) {
        this.userService = userService;
        this.accountHistoryService = accountHistoryService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = this.userService.saveUser(user);

        if(savedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<User> getUser(@RequestParam String email){
        User updatedUser = this.userService.getUserByEmail(email);

        if(updatedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/user")
    public ResponseEntity<User> updateUser(@RequestParam String email, @RequestParam String password) {
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        User oldUser = this.userService.getUserByEmail(user.getEmail());
        User updatedUser = this.userService.updateUser(user);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            this.accountHistoryService.makeNewEntry(oldUser, updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> saveUser(@RequestParam String firstname, @RequestParam String lastname,
                                           @RequestParam String email, @RequestParam String password,
                                           @RequestParam ERole role) {
        User userToSave = new User(firstname, lastname, email, password, role);
        User savedUser = this.userService.saveUser(userToSave);

        if (savedUser == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.OK);
        } else {
            try {
                File file = ResourceUtils.getFile("classpath:emailTemplateAccountConfirmation.txt");
                emailService.sendEmailFromTemplate(savedUser.getEmail(),
                        file.getPath(),
                        "UBB Account created", password);
            } catch (FileNotFoundException e) {
                System.out.println("email template not found");
            }
            return new ResponseEntity<>("User successfully created", HttpStatus.OK);
        }
    }
}
