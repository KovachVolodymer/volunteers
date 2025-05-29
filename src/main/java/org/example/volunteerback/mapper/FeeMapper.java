package org.example.volunteerback.mapper;

import org.example.volunteerback.dto.FeeDTO;
import org.example.volunteerback.dto.user.UserDTO;
import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class FeeMapper {

    public FeeDTO feeDTO(Fee fee){
        User user = fee.getUser();
        UserDTO userDTO = new UserDTO(user.getEmail(), user.getFirstName(), user.getLastName());
        return new FeeDTO(
                userDTO, fee.getTitle(),fee.getImage(),
                fee.getGoal(),fee.getRaised(),fee.getDescription(),
                fee.getCount()
        );
    }

}
