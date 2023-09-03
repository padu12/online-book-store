package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.user.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserLoginResponseDto;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;
import com.kaziamyr.onlinebookstore.service.AuthenticationService;
import com.kaziamyr.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication (login and registration)",
        description = "Endpoints for authenticating users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login the user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/registration")
    @Operation(summary = "Register the user")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request
    ) throws RegistrationException {
        return userService.register(request);
    }
}
