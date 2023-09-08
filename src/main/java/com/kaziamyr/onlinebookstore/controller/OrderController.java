package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.StatusRequestDto;
import com.kaziamyr.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto placeOrder(@RequestBody Map<String, String> requestBody) {
        return orderService.createOrder(requestBody);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<OrderDto> retrieveOrderHistory() {
        return orderService.findAllByUser();
    }

    @PatchMapping
    public OrderDto updateOrderStatus(StatusRequestDto statusDto) {
        return null;
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> retrieveAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return null;
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto retrieveOrderItemsById(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return null;
    }
}
