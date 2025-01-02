package org.example.volunteerback.service;

import org.example.volunteerback.dto.UserDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.mapper.UserMapper;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @Test
    void getUserSuccessful() {
        Long id = 0L;
        User user = new User("Vova", "Kovach", "kovach@gmail.com", "12345678");
        UserDTO userDTO = new UserDTO("Vova", "Kovach", "kovach@gmail.com", "12345678", "myPhoto", "Hi","0682590426");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        ResponseEntity<Object> response = userService.getUser(id);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userDTO,response.getBody());
    }

    @Test
    void getUser_NotFoundUser()
    {
        Long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class ,
                () -> userService.getUser(id));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUserSuccessful(){
        Long id = 0L;

        ResponseEntity<Object> response = userService.deleteUser(id);

        verify(userRepository).deleteById(id);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("User delete successfully", ((MessageResponse) Objects
                .requireNonNull(response.getBody())).message());
    }

}