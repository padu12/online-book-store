package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemWithBookTitleDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
