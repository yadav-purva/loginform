package com.validationlogin.controllers;

import com.validationlogin.entities.LoginRequest;
import com.validationlogin.entities.User;
import com.validationlogin.repository.UserRepo;
import com.validationlogin.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/auth")
    public class LoginController {

/*
    @Autowired
*/
        private final AuthService authService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    public LoginController(AuthService authService) {
            this.authService = authService;
        }

        @PostMapping("/getToken")
        public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

/*
        public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
            String token = authService.authenticateUser(username, password);
*/
            String token = authService.authenticateUser(loginRequest.getUsername(),loginRequest.getPassword());

            System.out.println(token);
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");  // Invalid credentials
            }
        }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {

        // Check if the user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(400).body("User already exists.");
        }

/*
        user.setPassword(authService.encodePassword(user.getPassword()));
*/
        user.setPassword(user.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok("User created successfully.");
    }


    @GetMapping("/getUser")
    public User getUserData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }


    }


