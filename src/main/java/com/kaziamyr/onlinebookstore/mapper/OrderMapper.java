package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.model.Order;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @BeforeMapping
    default void addUserId(
            @MappingTarget OrderDto orderDto, Order order
    ) {
        if (order != null) {
            orderDto.setUserId(order.getUser().getId());
        }
    }

    @Mapping(ignore = true, target = "orderItems")
    OrderDto toDto(Order order);
}
