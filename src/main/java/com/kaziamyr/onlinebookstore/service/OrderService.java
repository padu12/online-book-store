package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.dto.StatusRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto create(ShippingAddressDto shippingAddressDto);

    List<OrderDto> findAllByUser(Pageable pageable);

    OrderDto updateStatus(Long id, StatusRequestDto statusRequestDto);

    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    OrderItemDto getOrderItemByIdAndOrderId(Long itemId, Long orderId);
}
