package com.kaziamyr.onlinebookstore.dto.shoppingcart;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import com.kaziamyr.onlinebookstore.model.User;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private User user;
    private Set<CartItemWithBookTitleDto> cartItems = new HashSet<>();
}
