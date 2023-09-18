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
import com.kaziamyr.onlinebookstore.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;

    @Override
    public ShoppingCartDto getByUser() {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        return shoppingCartMapper.toDto(shoppingCart);
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
        shoppingCart.setCartItems(new HashSet<>(cartItems));
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(cartItem);
    }

    @Override
    public CartItemDto updateBookInShoppingCart(Long id, PutCartItemRequestDto request) {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getCartItems());
        CartItem presentCartItem = cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findAny().orElseThrow(
                        () -> new EntityNotFoundException("Can't update books count for"
                                + " cart item with id" + id)
                );
        presentCartItem.setQuantity(request.getQuantity());
        cartItems.set(cartItems.indexOf(presentCartItem), presentCartItem);
        shoppingCart.setCartItems(new HashSet<>(cartItems));
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(presentCartItem);
    }

    @Override
    public void deleteBookFromShoppingCartById(Long id) {
        cartItemRepository.deleteById(id);
    }

    public ShoppingCart getOrCreateUsersShoppingCart() {
        User user = userService.getCurrentUser();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findShoppingCartByUser(user);
        ShoppingCart shoppingCart;
        if (shoppingCartOptional.isEmpty()) {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setUser(user);
            shoppingCart = shoppingCartRepository.save(newShoppingCart);
        } else {
            shoppingCart = shoppingCartOptional.get();
            shoppingCart.setCartItems(
                    new HashSet<>(cartItemRepository.findAllByShoppingCart(shoppingCart)));
        }
        return shoppingCart;
    }
}
