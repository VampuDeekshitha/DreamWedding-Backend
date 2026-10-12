package com.tietheknot.controller;

import com.tietheknot.entity.User;
import com.tietheknot.repository.UserRepository;
import com.tietheknot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5174")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(
            UserService userService,
            UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody User user) {

        User savedUser = userService.registerUser(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(
            @RequestBody User user) {

        User existingUser = userRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!existingUser.getPassword()
                .equals(user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        return ResponseEntity.ok(existingUser);
    }
}