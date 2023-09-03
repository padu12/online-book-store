package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @BeforeMapping
    default void addUserId(
            @MappingTarget ShoppingCartDto shoppingCartDto, ShoppingCart shoppingCart
    ) {
        if (shoppingCart != null) {
            shoppingCartDto.setUserId(shoppingCart.getUser().getId());
        }
    }

    @Mapping(ignore = true, target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
