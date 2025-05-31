package org.example.volunteerback.controller;

import jakarta.validation.Valid;
import org.example.volunteerback.dto.user.JwtTokenPair;
import org.example.volunteerback.dto.user.UserAuthDTO;
import org.example.volunteerback.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtTokenPair> register(@RequestBody @Valid UserAuthDTO userAuthDTO) throws Exception {
        JwtTokenPair tokens = authService.register(userAuthDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, tokens.accessToken())
                .body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        return authService.login(userAuthDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refresh(@RequestBody Map<String, String> requestBody) {
        String refreshToken = requestBody.get("refreshToken");
        return authService.refresh(refreshToken);
    }

}
