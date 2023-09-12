package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @BeforeMapping
    default void addBookDetails(
            @MappingTarget CartItemWithBookTitleDto cartItemWithBookTitleDto, CartItem cartItem
    ) {
        Book book = cartItem.getBook();
        if (book != null && book.getId() != null && book.getTitle() != null) {
            cartItemWithBookTitleDto.setBookId(book.getId());
            cartItemWithBookTitleDto.setBookTitle(book.getTitle());
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
}
