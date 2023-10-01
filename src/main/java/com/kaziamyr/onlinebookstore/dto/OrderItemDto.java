package com.kaziamyr.onlinebookstore.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
