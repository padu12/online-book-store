package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderMapper;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.repository.OrderItemRepository;
import com.kaziamyr.onlinebookstore.repository.OrderRepository;
import com.kaziamyr.onlinebookstore.service.OrderService;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import com.kaziamyr.onlinebookstore.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final UserService userService;

    @Override
    public OrderDto create(ShippingAddressDto shippingAddressDto) {
        ShoppingCart shoppingCart = shoppingCartService.getOrCreateUsersShoppingCart();
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        newOrder.setStatus(Order.Status.PENDING);
        newOrder.setTotal(getTotalFromShoppingCart(shoppingCart));
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(shippingAddressDto.getShippingAddress());
        Set<OrderItem> orderItemSet = getOrderItemSet(shoppingCart, newOrder);
        newOrder.setOrderItems(orderItemSet);
        OrderDto orderDto = orderMapper.toDto(orderRepository.save(newOrder));
        orderDto.setOrderItems(orderItemSet.stream()
                .map(orderItemMapper::toDto)
                .toList());
        return orderDto;
    }

    private Set<OrderItem> getOrderItemSet(ShoppingCart shoppingCart, Order newOrder) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = cartItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(newOrder);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public List<OrderDto> findAllByUser(Pageable pageable) {
        List<Order> allOrders =
                orderRepository.findAllByUser(pageable, userService.getCurrentUser());
        return allOrders.stream()
                .map(order -> {
                    OrderDto orderDto = orderMapper.toDto(order);
                    orderDto.setOrderItems(getOrderItemDtosByOrder(order));
                    return orderDto;
                })
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long id, ShippingAddressDto shippingAddressDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id " + id)
        );
        order.setStatus(Order.Status.valueOf(shippingAddressDto.getShippingAddress()));
        order.setOrderItems(
                new HashSet<>(orderItemRepository.getOrderItemsByOrderId(order.getId())));
        orderRepository.save(order);
        OrderDto orderDto = orderMapper.toDto(order);
        orderDto.setOrderItems(getOrderItemDtosByOrder(order));
        return orderDto;
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, userService.getCurrentUser())
                .orElseThrow(
                        () -> new EntityNotFoundException("You don't have order with id " + orderId)
                );
        return getOrderItemDtosByOrder(order);
    }

    @Override
    public OrderItemDto getOrderItemByIdAndOrderId(Long itemId, Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, userService.getCurrentUser())
                .orElseThrow(
                        () -> new EntityNotFoundException("You don't have order with id " + orderId)
                );
        List<OrderItemDto> orderItemDtos = getOrderItemDtosByOrder(order);
        return orderItemDtos.stream()
                .filter(orderItemDto -> Objects.equals(orderItemDto.getId(), itemId))
                .findFirst().orElseThrow(
                        () -> new EntityNotFoundException("You don't have order item with id "
                                + itemId + " in order with id " + orderId));
    }

    private List<OrderItemDto> getOrderItemDtosByOrder(Order order) {
        return orderItemRepository.getOrderItemsByOrderId(order.getId()).stream()
                .map(orderItemMapper::toDto)
                .toList();
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
