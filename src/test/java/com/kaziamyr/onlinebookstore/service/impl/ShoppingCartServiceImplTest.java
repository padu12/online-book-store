package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.ShoppingCartMapper;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CartItemRepository;
import com.kaziamyr.onlinebookstore.repository.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @Test
    @DisplayName("Test getByUser() with a valid user")
    void getByUser_validUser_returnShoppingCartDto() {
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
//        when(shoppingCartRepository.findShoppingCartByUser(any())).thenReturn();
    }

    @Test
    void addBookToShoppingCart() {
    }

    @Test
    void updateBookInShoppingCart() {
    }

    @Test
    void deleteBookFromShoppingCartById() {
    }

    @Test
    void getOrCreateUsersShoppingCart() {
    }
}