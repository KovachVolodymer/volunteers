package org.example.volunteerback.mapper;

import org.example.volunteerback.dto.response.UserResponseDTO;
import org.example.volunteerback.model.user.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoto(), user.getDescription()
        );
    }

}
