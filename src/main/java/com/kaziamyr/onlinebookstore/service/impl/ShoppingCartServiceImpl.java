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
    public CartItemDto addBook(CreateCartItemRequestDto request) {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        List<CartItem> cartItems = getCartItems(shoppingCart);
        CartItem cartItem;
        Optional<CartItem> optionalPresentCartItem = cartItems.stream()
                .filter(cartItemFromShoppingCart ->
                        cartItemFromShoppingCart.getBook().getId().equals(request.getBookId()))
                .findAny();
        if (optionalPresentCartItem.isPresent()) {
            cartItem = addQuantity(request, optionalPresentCartItem, cartItems);
        } else {
            cartItem = addNewBook(request, shoppingCart, cartItems);
        }
        shoppingCart.setCartItems(new HashSet<>(cartItems));
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(cartItem);
    }

    private static ArrayList<CartItem> getCartItems(ShoppingCart shoppingCart) {
        if (shoppingCart.getCartItems().isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(shoppingCart.getCartItems());
        }
    }

    @Override
    public CartItemDto updateBook(Long id, PutCartItemRequestDto request) {
        ShoppingCart shoppingCart = getOrCreateUsersShoppingCart();
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getCartItems());
        CartItem presentCartItem = findCardItemById(id, cartItems);
        presentCartItem.setQuantity(request.getQuantity());
        cartItems.set(cartItems.indexOf(presentCartItem), presentCartItem);
        shoppingCart.setCartItems(new HashSet<>(cartItems));
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toCartItemDto(presentCartItem);
    }

    private static CartItem findCardItemById(Long id, List<CartItem> cartItems) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findAny().orElseThrow(
                        () -> new EntityNotFoundException("Can't update books count for"
                                + " cart item with id" + id)
                );
    }

    @Override
    public void deleteBookById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public ShoppingCart getOrCreateUsersShoppingCart() {
        User user = userService.getCurrentUser();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findShoppingCartByUser(user);
        ShoppingCart shoppingCart;
        if (shoppingCartOptional.isEmpty()) {
            shoppingCart = getNewShoppingCart(user);
        } else {
            shoppingCart = getPresentShoppingCart(shoppingCartOptional);
        }
        return shoppingCart;
    }

    private ShoppingCart getPresentShoppingCart(Optional<ShoppingCart> shoppingCartOptional) {
        ShoppingCart shoppingCart;
        shoppingCart = shoppingCartOptional.get();
        shoppingCart.setCartItems(
                new HashSet<>(cartItemRepository.findAllByShoppingCart(shoppingCart)));
        return shoppingCart;
    }

    private ShoppingCart getNewShoppingCart(User user) {
        ShoppingCart shoppingCart;
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        shoppingCart = shoppingCartRepository.save(newShoppingCart);
        return shoppingCart;
    }

    private static CartItem addQuantity(
            CreateCartItemRequestDto request,
            Optional<CartItem> optionalPresentCartItem,
            List<CartItem> cartItems
    ) {
        CartItem cartItem;
        cartItem = optionalPresentCartItem.get();
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItems.set(cartItems.indexOf(optionalPresentCartItem.get()), cartItem);
        return cartItem;
    }

    private CartItem addNewBook(
            CreateCartItemRequestDto request,
            ShoppingCart shoppingCart,
            List<CartItem> cartItems
    ) {
        CartItem cartItem;
        cartItem = cartItemMapper.toModel(request);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookRepository.findBookById(request.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id "
                        + request.getBookId())
        ));
        cartItem = cartItemRepository.save(cartItem);
        cartItems.add(cartItem);
        return cartItem;
    }
}
