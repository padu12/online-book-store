package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class ShoppingCartMapper {
    @Autowired
    private CartItemMapper cartItemMapper;

    @BeforeMapping
    public void addUserId(
            @MappingTarget ShoppingCartDto shoppingCartDto, ShoppingCart shoppingCart
    ) {
        if (shoppingCart != null) {
            shoppingCartDto.setUserId(shoppingCart.getUser().getId());
            shoppingCartDto.setCartItems(
                    shoppingCart.getCartItems().stream()
                            .map(cartItemMapper::toCartItemWithBookTitle)
                            .toList()
            );
        }
    }

    @Mapping(ignore = true, target = "cartItems")
    public abstract ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
