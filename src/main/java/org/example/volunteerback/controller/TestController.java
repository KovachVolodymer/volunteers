package org.example.volunteerback.controller;

import org.example.volunteerback.dto.response.MessageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping
    public Optional<Object> gets()
    {
        return Optional.of(new MessageResponse("Hello"));
    }

}
