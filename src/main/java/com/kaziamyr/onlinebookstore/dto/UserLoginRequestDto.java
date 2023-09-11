package com.kaziamyr.onlinebookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull
    @Email
    @Size(min = 1)
    private String email;
    @NotNull
    @Size(min = 8)
    private String password;
}
