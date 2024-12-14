package org.example.volunteerback.controller;

import org.example.volunteerback.dto.request.LoginRequest;
import org.example.volunteerback.dto.request.RegisterRequest;
import org.example.volunteerback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) throws Exception {
       return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }

}
