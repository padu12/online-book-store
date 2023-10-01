package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
