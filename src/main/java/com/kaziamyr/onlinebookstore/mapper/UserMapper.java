package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.kaziamyr.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.kaziamyr.onlinebookstore.model.User;
import java.util.Set;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract UserRegistrationResponseDto toUserResponseDto(User user);

    @BeforeMapping
    public void addPasswordAndRoles(
            @MappingTarget User user, UserRegistrationRequestDto requestDto
    ) {
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(new Role(1L, Role.RoleName.ROLE_USER)));
    }

    public abstract User toModel(UserRegistrationRequestDto requestDto);
}
