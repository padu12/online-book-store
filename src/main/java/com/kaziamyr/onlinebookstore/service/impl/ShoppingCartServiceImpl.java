package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.ShoppingCartMapper;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CartItemRepository;
import com.kaziamyr.onlinebookstore.repository.ShoppingCartRepository;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getShoppingCartByUser() {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(
                shoppingCart.getCartItems().stream()
                        .map(cartItemMapper::toCartItemWithBookTitle)
                        .toList()
        );
        return shoppingCartDto;
    }

    @Override
    public CartItemDto addBookToShoppingCart(CreateCartItemRequestDto request) {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        CartItem cartItem = cartItemMapper.toModel(request);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookRepository.getBookById(request.getBookId()));
        cartItem = cartItemRepository.save(cartItem);
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getCartItems());
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(cartItem);
    }

    private ShoppingCart getOrCreateUsersShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findShoppingCartByUser(user);
        ShoppingCart shoppingCart;
        if (shoppingCartOptional.isEmpty()) {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setUser(user);
            shoppingCart = shoppingCartRepository.save(newShoppingCart);
        } else {
            shoppingCart = shoppingCartOptional.get();
        }
        return shoppingCart;
    }
}
