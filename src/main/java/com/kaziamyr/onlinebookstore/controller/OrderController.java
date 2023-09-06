package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.OrderDTO;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.dto.StatusDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @PostMapping
    public void placeOrder(ShippingAddressDto shippingAddressDto) {

    }

    @GetMapping
    public List<OrderDTO> retrieveOrderHistory() {
        return null;
    }

    @PatchMapping
    public void updateOrderStatus(StatusDto statusDto) {

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
