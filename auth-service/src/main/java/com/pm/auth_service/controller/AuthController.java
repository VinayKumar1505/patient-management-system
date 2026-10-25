package com.pm.auth_service.controller;

import com.pm.auth_service.dto.LoginRequest;
import com.pm.auth_service.dto.LoginResponse;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.pm.auth_service.service.AuthService;

@RestController
public class AuthController {
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        
        Optional<String> token = authService.authenticateUser(loginRequest);
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new LoginResponse(token.get()));
    }
}