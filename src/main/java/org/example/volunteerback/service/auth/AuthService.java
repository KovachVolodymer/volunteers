package org.example.volunteerback.service.auth;

import org.example.volunteerback.config.jwt.JwtUtils;
import org.example.volunteerback.dto.user.UserAuthDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.exception.EmailAlreadyExistsException;
import org.example.volunteerback.factory.UserFactory;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.ERole;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.model.user.UserDetailsServiceImpl;
import org.example.volunteerback.repository.RoleRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserFactory userFactory;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationService authenticationService, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userFactory = userFactory;
    }

    public ResponseEntity<Object> register(UserAuthDTO request) throws Exception {

        validateEmail(request.email());

        User user = userFactory.createUser(request);

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ERole USER not found"));


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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid refresh token." + e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token validation failed."));
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

}
