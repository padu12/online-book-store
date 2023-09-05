package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getUsersShoppingCart() {
        return shoppingCartService.getShoppingCartByUser();
    }

    @PostMapping
    public CartItemDto addBookToShoppingCart(@RequestBody CreateCartItemRequestDto request) {
        return shoppingCartService.addBookToShoppingCart(request);
    }

    @PutMapping("/cart-items/{id}")
    public CartItemDto updateBooksInShoppingCart(@PathVariable Long id,
                                                 @RequestBody PutCartItemRequestDto request) {
        return shoppingCartService.updateBookInShoppingCart(id, request);
    }

    @DeleteMapping("/cart-items/{id}")
    public void deleteBookFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteBookFromShoppingCartById(id);
    }
}
