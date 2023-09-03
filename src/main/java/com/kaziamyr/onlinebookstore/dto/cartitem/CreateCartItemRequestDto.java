package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long book_id;
    private int quantity;
}
