package org.example.volunteerback.mapper;

import org.example.volunteerback.dto.UserAuthDTO;
import org.example.volunteerback.dto.UserDTO;
import org.example.volunteerback.model.user.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoto(), user.getDescription()
        );
    }

}
