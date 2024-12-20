package org.example.volunteerback.service;

import org.example.volunteerback.config.jwt.JwtUtils;
import org.example.volunteerback.dto.UserAuthDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.model.user.UserDetailsServiceImpl;
import org.example.volunteerback.repository.RoleRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationService authenticationService, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public ResponseEntity<Object> register(UserAuthDTO request) throws Exception {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email exists"));
        }
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        Map<String, String> tokens = authenticationService.authenticateAndGenerateTokens(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, tokens.get("accessToken"))
                .body(Map.of(
                        "message", "Register successful",
                        "refreshToken", tokens.get("refreshToken")
                ));
    }

    public ResponseEntity<Object> login(UserAuthDTO request) throws Exception {
        if (!userRepository.existsByEmail(request.email())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email or password is incorrect!"));
        }

        Map<String, String> tokens = authenticationService.authenticateAndGenerateTokens(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, tokens.get("accessToken"))
                .body(Map.of(
                "message", "Login successful",
                "refreshToken", tokens.get("refreshToken")
        ));
    }

    public ResponseEntity<Object> refresh(String refreshToken) {
        try {
            if (jwtUtils.validateJwt(refreshToken)) {
                String email = jwtUtils.getUserNameFromJwtToken(refreshToken);

                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

                String newAccessToken = jwtUtils.generateJwtToken(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "accessToken", newAccessToken
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid refresh token."+ e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token validation failed."));
    }

}
