package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.dto.StatusRequestDto;
import com.kaziamyr.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders and their items")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Place user's order according items in shopping cart")
    public OrderDto place(@RequestBody ShippingAddressDto shippingAddressDto) {
        return orderService.create(shippingAddressDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Retrieve user's order history")
    public List<OrderDto> retrieveUsersHistory(Pageable pageable) {
        return orderService.findAllByUser(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update status of order by its id")
    public OrderDto updateStatus(
            @PathVariable Long id,
            @RequestBody StatusRequestDto statusRequestDto
    ) {
        return orderService.updateStatus(id, statusRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Retrieve all order items from a specific order")
    public List<OrderItemDto> getAllByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve a specific item from a specific order")
    public OrderItemDto getByIdAndItemId(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getOrderItemByIdAndOrderId(itemId, orderId);
    }
}
