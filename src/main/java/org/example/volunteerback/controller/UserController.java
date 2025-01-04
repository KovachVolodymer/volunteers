package org.example.volunteerback.controller;

import jakarta.validation.Valid;
import org.example.volunteerback.dto.UserDTO;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.example.volunteerback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUser(userDetails.getId());
    }

    @PatchMapping
    public ResponseEntity<Object> patchUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody @Valid UserDTO userDTO) {
        return userService.patchUser(userDetails.getId(), userDTO);
    }
    
    @DeleteMapping
    private ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(userDetails.getId());
    }

}
