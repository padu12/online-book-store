package com.kaziamyr.onlinebookstore.dto.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PutCartItemRequestDto {
    private int quantity;
}
