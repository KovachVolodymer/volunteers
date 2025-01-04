package org.example.volunteerback.service;

import jakarta.validation.Valid;
import org.example.volunteerback.dto.UserDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.mapper.UserMapper;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.RoleRepository;
import org.example.volunteerback.repository.UserRepository;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    public final UserMapper userMapper;
    private final RoleRepository roleRepository;


    public UserService(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Object> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = user.map(userMapper::toDTO)
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
        Optional.ofNullable(updateDTO.phone()).ifPresent(user::setPhone);
        if (userRepository.existsByEmail(updateDTO.email()) && !Objects.equals(updateDTO.email(), user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));
        }
        Optional.ofNullable(updateDTO.email()).ifPresent(user::setEmail);


        if (updateDTO.previousPassword() != null && (!encoder.matches(updateDTO.previousPassword(), user.getPassword()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Wrong password"));
        }

        Optional.ofNullable(updateDTO.password())
                .ifPresent(newPassword -> user.setPassword(encoder.encode(newPassword)));


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    public ResponseEntity<Object> deleteUser(Long id)
    {
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse("User delete successfully"));
    }

    public ResponseEntity<Object> addRoleAdmin(Long id){
      Optional<User> optionalUser =  userRepository.findById(id);
      if (optionalUser.isPresent()){
          User userData= optionalUser.get();
          Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                  .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
          userData.getRoles().add(adminRole);
          userRepository.save(userData);
      }
      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role add successfully"));
    }

}
