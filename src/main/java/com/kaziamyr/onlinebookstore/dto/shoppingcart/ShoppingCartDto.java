package com.kaziamyr.onlinebookstore.dto.shoppingcart;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemWithBookTitleDto> cartItems;
}
