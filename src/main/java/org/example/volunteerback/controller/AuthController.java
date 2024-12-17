package org.example.volunteerback.controller;

import jakarta.validation.Valid;
import org.example.volunteerback.dto.UserAuthDTO;
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
    public ResponseEntity<Object> register(@RequestBody @Valid UserAuthDTO userAuthDTO) throws Exception {
        return authService.register(userAuthDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        return authService.login(userAuthDTO);
    }

}
