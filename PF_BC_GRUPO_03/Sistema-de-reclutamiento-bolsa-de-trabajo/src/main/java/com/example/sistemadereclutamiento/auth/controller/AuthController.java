package com.example.sistemadereclutamiento.auth.controller;


import com.example.sistemadereclutamiento.auth.dto.LoginRequest;
import com.example.sistemadereclutamiento.auth.dto.RefreshRequest;
import com.example.sistemadereclutamiento.auth.dto.TokenResponse;
import com.example.sistemadereclutamiento.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @PostMapping("/refresh")
    public  ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refresh(refreshRequest.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout ( @RequestBody RefreshRequest refreshRequest) {
        authService.logout(refreshRequest.refreshToken());
        return ResponseEntity.ok().build();
    }




}
