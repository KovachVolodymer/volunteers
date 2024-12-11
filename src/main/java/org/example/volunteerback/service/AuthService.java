package org.example.volunteerback.service;

import org.example.volunteerback.dto.request.RegisterRequest;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.RoleRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Object> register(RegisterRequest request){
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

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Successful registration"));
    }



}
