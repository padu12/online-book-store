package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.user.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto request);
}
