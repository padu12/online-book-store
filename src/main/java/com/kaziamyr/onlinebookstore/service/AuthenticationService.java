package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto request);
}
