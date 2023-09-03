package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;

@Data
public class CartItemWithBookTitleDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
