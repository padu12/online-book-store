package com.kaziamyr.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
