package com.riya.expense_tracker.controller;

import com.riya.expense_tracker.model.User;
import com.riya.expense_tracker.model.Role;
import com.riya.expense_tracker.repository.UserRepository;
import com.riya.expense_tracker.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Default role is VIEWER if not specified
        if (user.getRole() == null) user.setRole(Role.VIEWER);
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(username, user.getRole().name());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole().name());
            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}