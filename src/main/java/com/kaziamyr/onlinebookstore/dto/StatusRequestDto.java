package com.kaziamyr.onlinebookstore.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StatusRequestDto {
    private String status;
}
