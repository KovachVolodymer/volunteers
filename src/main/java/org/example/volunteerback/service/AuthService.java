package org.example.volunteerback.service;

import org.example.volunteerback.config.jwt.JwtUtils;
import org.example.volunteerback.dto.UserAuthDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<Object> register(UserAuthDTO request) throws Exception {
        if(userRepository.existsByEmail(request.email()))
        {
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

        return setAuthentication(request,"Register successful");
    }

    public ResponseEntity<Object> login(UserAuthDTO request) throws Exception {
        if (!userRepository.existsByEmail(request.email()) ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email or password is incorrect!"));
        }

      return setAuthentication(request,"Login successful");
    }

    private ResponseEntity<Object> setAuthentication (UserAuthDTO request, String message) throws Exception {
        Authentication authentication;
        try {
            authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.email(), request.password()
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Email or password is incorrect!"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .body(new MessageResponse(message));
    }


}
