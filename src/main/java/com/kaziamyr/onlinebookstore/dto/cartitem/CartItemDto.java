package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long book_id;
    private int quantity;
}
