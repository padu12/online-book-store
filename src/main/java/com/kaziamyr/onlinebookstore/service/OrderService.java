package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto create(Map<String, String> requestBody);

    List<OrderDto> findAllByUser(Pageable pageable);

    OrderDto updateStatus(Long id, ShippingAddressDto shippingAddressDto);

    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    OrderItemDto getOrderItemByIdAndOrderId(Long itemId, Long orderId);
}
