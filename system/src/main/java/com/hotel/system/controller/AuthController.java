package com.hotel.system.controller;

import com.hotel.system.domain.dto.request.AuthRequest;
import com.hotel.system.domain.dto.response.AuthResponse;
import com.hotel.system.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    //PRUEBAS
    @GetMapping("/generate-password")
    public ResponseEntity<String> generatePassword(@RequestParam String password) {
        String encoded = new BCryptPasswordEncoder().encode(password);
        return ResponseEntity.ok("Password: " + password + "\nEncoded: " + encoded);
    }
}