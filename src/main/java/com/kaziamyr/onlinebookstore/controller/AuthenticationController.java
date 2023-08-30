package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserResponseDto;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {
    @GetMapping("login")
    public UserResponseDto login(UserLoginRequestDto request) {
        return null;// TODO add service method
    }

    @PostMapping("/registration")
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        return null;// TODO add service method
    }
}
