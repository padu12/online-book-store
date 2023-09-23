package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.exception.RegistrationException;
import com.kaziamyr.onlinebookstore.mapper.UserMapper;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.UserRepository;
import com.kaziamyr.onlinebookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("User with email "
                    + request.getEmail() + " is already present!");
        }
        User savedUser = userRepository.save(userMapper.toModel(request));
        savedUser.setRoles(Set.of(new Role(1L, Role.RoleName.ROLE_USER)));
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with email "
                        + authentication.getName())
        );
    }
}
