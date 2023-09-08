package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDto createOrder(Map<String, String> requestBody);

    List<OrderDto> findAllByUser();
}
