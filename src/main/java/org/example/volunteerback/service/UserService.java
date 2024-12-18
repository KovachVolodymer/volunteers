package org.example.volunteerback.service;

import jakarta.validation.Valid;
import org.example.volunteerback.dto.UserAuthDTO;
import org.example.volunteerback.dto.UserDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.mapper.UserMapper;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.UserRepository;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<Object> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = user.map(UserMapper::toDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(userDTO);
    }

    public ResponseEntity<Object> patchUser(Long id, @Valid UserDTO updateDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User not found"));
        }
        User user = optionalUser.get();

        Optional.ofNullable(updateDTO.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateDTO.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(updateDTO.photo()).ifPresent(user::setPhoto);
        Optional.ofNullable(updateDTO.description()).ifPresent(user::setDescription);
        if (userRepository.existsByEmail(updateDTO.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));
        }
        Optional.ofNullable(updateDTO.email()).ifPresent(user::setEmail);
        Optional.ofNullable(updateDTO.password()).ifPresent(p -> user.setPassword(encoder.encode(p)));


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    public ResponseEntity<Object> deleteUser(Long id)
    {
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse("User delete successfully"));
    }

}
