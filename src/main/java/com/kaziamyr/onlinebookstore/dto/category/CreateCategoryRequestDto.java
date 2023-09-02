package com.kaziamyr.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotNull
    @Size(min = 1, max = 100)
    private String title;
    private String description;
}
