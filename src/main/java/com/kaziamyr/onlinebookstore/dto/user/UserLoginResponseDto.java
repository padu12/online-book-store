package com.kaziamyr.onlinebookstore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLoginResponseDto {
    private String token;
}
