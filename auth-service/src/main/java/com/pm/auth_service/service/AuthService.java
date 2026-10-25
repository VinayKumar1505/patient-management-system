package com.pm.auth_service.service;

import com.pm.auth_service.dto.LoginRequest;
import com.pm.auth_service.util.JwtUtil;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public Optional<String> authenticateUser(LoginRequest loginRequest) {
        Optional<String> token= userService.findByEmail(loginRequest.getEmail())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .map(user -> jwtUtil.generateToken(user.getEmail(), user.getRole()));
        return token;
    }
}
