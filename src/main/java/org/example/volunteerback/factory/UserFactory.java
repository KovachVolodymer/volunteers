package org.example.volunteerback.factory;

import org.example.volunteerback.dto.user.UserAuthDTO;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserAuthDTO authDTO) {
        return User.builder()
                .firstName(authDTO.firstName())
                .lastName(authDTO.lastName())
                .email(authDTO.email())
                .password(passwordEncoder.encode(authDTO.password()))
                .build();
    }


}
