package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get users shopping cart")
    public ShoppingCartDto getUsersShoppingCart() {
        return shoppingCartService.getByUser();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Add books to the shopping cart",
            description = "If there are the same books program will add quantity")
    public CartItemDto addBookToShoppingCart(@RequestBody CreateCartItemRequestDto request) {
        return shoppingCartService.addBookToShoppingCart(request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update quantity of books in the shopping cart")
    public CartItemDto updateBooksInShoppingCart(@PathVariable Long id,
                                                 @RequestBody PutCartItemRequestDto request) {
        return shoppingCartService.updateBookInShoppingCart(id, request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete books from teh shopping cart")
    public void deleteBookFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteBookFromShoppingCartById(id);
    }
}
