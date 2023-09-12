package com.kaziamyr.onlinebookstore.dto;

import lombok.Data;

@Data
public class UserRegistrationResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shoppingAddress;
}
