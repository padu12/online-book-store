package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
