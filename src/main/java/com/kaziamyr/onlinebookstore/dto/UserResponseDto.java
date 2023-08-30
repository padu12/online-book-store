package com.kaziamyr.onlinebookstore.dto;

import com.kaziamyr.onlinebookstore.model.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shoppingAddress;
    private Role role;
}
