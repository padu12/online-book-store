package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDto create(Map<String, String> requestBody);

    List<OrderDto> findAllByUser();

    OrderDto updateStatus(Long id, Map<String, String> requestBody);
}
