package com.kaziamyr.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SaveCategoryRequestDto {
    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    private String description;
}
