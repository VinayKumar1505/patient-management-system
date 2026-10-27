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
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestHeader;

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

    //validate token
    @Operation(summary = "Validate JWT token", description = "Validates the provided JWT token and returns a 200 OK response if valid, or a 401 Unauthorized response if invalid.")
    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
       
        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
    }
}