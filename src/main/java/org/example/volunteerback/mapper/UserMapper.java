package org.example.volunteerback.mapper;

import org.example.volunteerback.dto.user.UserDTO;
import org.example.volunteerback.model.user.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public  UserDTO toDTO(User user) {
        return new UserDTO(
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoto(), user.getDescription(), user.getPhone()
        );
    }

}
