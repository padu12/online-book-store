package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderMapper;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.OrderItemRepository;
import com.kaziamyr.onlinebookstore.repository.OrderRepository;
import com.kaziamyr.onlinebookstore.service.OrderService;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final CartItemMapper cartItemMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto create(Map<String, String> requestBody) {
        ShoppingCart shoppingCart = shoppingCartService.getOrCreateUsersShoppingCart();
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        newOrder.setStatus(Order.Status.PENDING);
        newOrder.setTotal(getTotalFromShoppingCart(shoppingCart));
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(requestBody.get("shippingAddress"));
        Set<OrderItem> orderItemSet = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = cartItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(newOrder);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        newOrder.setOrderItems(orderItemSet);
        orderRepository.save(newOrder);
        orderItemRepository.saveAll(orderItemSet);
        OrderDto orderDto = orderMapper.toDto(newOrder);
        orderDto.setOrderItems(orderItemSet.stream()
                .map(orderItemMapper::toDto)
                .toList());
        return orderDto;
    }

    @Override
    public List<OrderDto> findAllByUser() {
        List<Order> allOrders = orderRepository.findAllByUser(getCurrentUser());
        return allOrders.stream()
                .map(order -> {
                    OrderDto orderDto = orderMapper.toDto(order);
                    orderDto.setOrderItems(getOrderItemsByOrder(order));
                    return orderDto;
                })
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long id, Map<String, String> requestBody) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id " + id)
        );
        order.setStatus(Order.Status.valueOf(requestBody.get("status")));
        order.setOrderItems(
                new HashSet<>(orderItemRepository.getOrderItemsByOrderId(order.getId())));
        orderRepository.save(order);
        OrderDto orderDto = orderMapper.toDto(order);
        orderDto.setOrderItems(getOrderItemsByOrder(order));
        return orderDto;
    }

    private List<OrderItemDto> getOrderItemsByOrder(Order order) {
        return orderItemRepository.getOrderItemsByOrderId(order.getId()).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    private static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    private BigDecimal getTotalFromShoppingCart(ShoppingCart shoppingCart) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            BigDecimal priceForACartItem =
                    cartItem.getBook().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            total = total.add(priceForACartItem);
        }
        return total;
    }
}
