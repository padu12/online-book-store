package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
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
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getCartItems());
        CartItem cartItem;
        Optional<CartItem> optionalPresentCartItem = cartItems.stream()
                .filter(cartItem1 -> cartItem1.getBook().getId().equals(request.getBookId()))
                .findAny();
        if (optionalPresentCartItem.isPresent()) {
            cartItem = optionalPresentCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItems.set(cartItems.indexOf(optionalPresentCartItem.get()), cartItem);
        } else {
            cartItem = cartItemMapper.toModel(request);
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(bookRepository.findBookById(request.getBookId()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find book with id "
                            + request.getBookId())
            ));
            cartItem = cartItemRepository.save(cartItem);
            cartItems.add(cartItem);
        }
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(cartItem);
    }

    @Override
    public CartItemDto updateBookInShoppingCart(Long id, PutCartItemRequestDto request) {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getCartItems());
        CartItem presentCartItem = cartItems.stream()
                .filter(cartItem1 -> cartItem1.getId().equals(id))
                .findAny().orElseThrow(
                        () -> new EntityNotFoundException("Can't update books count for"
                                + " cart item with id" + id)
                );
        presentCartItem.setQuantity(request.getQuantity());
        cartItems.set(cartItems.indexOf(presentCartItem), presentCartItem);
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(presentCartItem);
    }

    @Override
    public void deleteBookFromShoppingCartById(Long id) {
        cartItemRepository.deleteById(id);
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
            shoppingCart.setCartItems(cartItemRepository.findAllByShoppingCart(shoppingCart));
        }
        return shoppingCart;
    }
}
