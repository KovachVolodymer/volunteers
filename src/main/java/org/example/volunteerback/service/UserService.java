package org.example.volunteerback.service;

import org.example.volunteerback.dto.response.UserResponseDTO;
import org.example.volunteerback.mapper.UserMapper;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.UserRepository;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        UserResponseDTO userResponseDTO = user.map(UserMapper::toDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(userResponseDTO);
    }



}
