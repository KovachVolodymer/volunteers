package org.example.volunteerback.controller;

import org.example.volunteerback.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("api/users")
public class UserControllerAdmin {

    private final UserService userService;

    @Autowired
    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addRole/{id}")
    public ResponseEntity<Object> addRole(@PathVariable Long id) {
        return userService.addRoleAdmin(id);
    }
}
