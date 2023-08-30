package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserResponseDto;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;
import com.kaziamyr.onlinebookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {
    private final UserService userService;

    @GetMapping("login")
    public UserResponseDto login(UserLoginRequestDto request) {
        return null;// TODO add service method
    }

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
