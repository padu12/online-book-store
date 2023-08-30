package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserResponseDto;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
