package com.kaziamyr.onlinebookstore.dto;

import com.kaziamyr.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotNull
    @Email
    @Size(min = 1, max = 100)
    private String email;
    @NotNull
    @Size(min = 8, max = 30)
    private String password;
    @NotNull
    private String repeatPassword;
    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;
    private String shoppingAddress;
}
