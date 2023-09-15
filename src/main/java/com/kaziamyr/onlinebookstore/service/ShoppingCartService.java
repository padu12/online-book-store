package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto getByUser();

    CartItemDto addBook(CreateCartItemRequestDto request);

    CartItemDto updateBook(Long id, PutCartItemRequestDto request);

    void deleteBookById(Long id);

    ShoppingCart getOrCreateUsersShoppingCart();
}
