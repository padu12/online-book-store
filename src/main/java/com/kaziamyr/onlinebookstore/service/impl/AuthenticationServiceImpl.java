package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.UserLoginRequestDto;
import com.kaziamyr.onlinebookstore.dto.UserLoginResponseDto;
import com.kaziamyr.onlinebookstore.security.JwtUtil;
import com.kaziamyr.onlinebookstore.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        return new UserLoginResponseDto(token);
    }
}
