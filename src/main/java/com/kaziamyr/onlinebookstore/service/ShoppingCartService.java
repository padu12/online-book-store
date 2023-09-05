package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUser();

    CartItemDto addBookToShoppingCart(CreateCartItemRequestDto request);

    CartItemDto updateBookInShoppingCart(Long id, PutCartItemRequestDto request);

    void deleteBookFromShoppingCartById(Long id);
}
