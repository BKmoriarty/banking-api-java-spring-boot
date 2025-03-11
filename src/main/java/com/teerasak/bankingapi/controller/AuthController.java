package com.teerasak.bankingapi.controller;

import com.teerasak.bankingapi.dto.auth.LoginRequest;
import com.teerasak.bankingapi.dto.auth.LoginResponse;
import com.teerasak.bankingapi.dto.auth.RegisterRequest;
import com.teerasak.bankingapi.usecase.auth.LoginUser;
import com.teerasak.bankingapi.usecase.auth.RegisterUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final RegisterUser registerUser;
    private final LoginUser loginUser;

    public AuthController(RegisterUser registerUser, LoginUser loginUser) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        registerUser.execute(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = loginUser.execute(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}