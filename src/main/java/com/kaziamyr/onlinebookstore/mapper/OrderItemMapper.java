package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @BeforeMapping
    default void addBookId(
            @MappingTarget OrderItemDto orderItemDto, OrderItem orderItem
    ) {
        if (orderItem != null) {
            orderItemDto.setBookId(orderItem.getBook().getId());
        }
    }

    OrderItemDto toDto(OrderItem orderItem);
}
