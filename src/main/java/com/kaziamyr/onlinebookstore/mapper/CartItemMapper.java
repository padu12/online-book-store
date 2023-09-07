package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @BeforeMapping
    default void addBookDetails(
            @MappingTarget CartItemWithBookTitleDto cartItemWithBookTitleDto, CartItem cartItem
    ) {
        Book book = cartItem.getBook();
        if (book != null) {
            if (book.getId() != null) {
                cartItemWithBookTitleDto.setBookId(book.getId());
            }
            if (book.getTitle() != null) {
                cartItemWithBookTitleDto.setBookTitle(book.getTitle());
            }
        }
    }

    CartItemWithBookTitleDto toCartItemWithBookTitle(CartItem cartItem);

    CartItem toModel(CreateCartItemRequestDto requestDto);

    @BeforeMapping
    default void addBookId(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        Book book = cartItem.getBook();
        if (book != null) {
            cartItemDto.setBookId(book.getId());
        }
    }

    CartItemDto toCartItemDto(CartItem cartItem);

    @BeforeMapping
    default void addPrice(@MappingTarget OrderItem orderItem, CartItem cartItem) {
        Book book = cartItem.getBook();
        if (book != null) {
            BigDecimal quantityOfBooks = BigDecimal.valueOf(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice().multiply(quantityOfBooks));
        }
    }

    @Mapping(ignore = true, target = "id")
    OrderItem toOrderItem(CartItem cartItem);
}
