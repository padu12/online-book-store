package com.kaziamyr.onlinebookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.dto.StatusRequestDto;
import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderItemMapper;
import com.kaziamyr.onlinebookstore.mapper.OrderMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.OrderItemRepository;
import com.kaziamyr.onlinebookstore.repository.OrderRepository;
import com.kaziamyr.onlinebookstore.service.ShoppingCartService;
import com.kaziamyr.onlinebookstore.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final Role ROLE_USER = new Role()
            .setId(1L)
            .setRoleName(Role.RoleName.ROLE_USER);
    private static final Category FANTASY = new Category()
            .setId(1L)
            .setName("Fantasy");
    private static final Book ZAKHAR_BERKUT = new Book()
            .setId(1L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9781691430703")
            .setPrice(new BigDecimal("20"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(FANTASY));
    private static final Book LISOVA_PISNIA = new Book()
            .setId(2L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("10"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(FANTASY));
    private static final User USER = new User()
            .setId(1L)
            .setEmail("user@example.com")
            .setPassword("your_password")
            .setFirstName("John")
            .setLastName("Doe")
            .setShoppingAddress("123 Main Street")
            .setRoles(Set.of(ROLE_USER));
    private static final CartItem ZAKHAR_BERKUT_CART_ITEM = new CartItem()
            .setId(1L)
            .setShoppingCart(new ShoppingCart())
            .setBook(ZAKHAR_BERKUT)
            .setQuantity(5);
    private static final CartItem LISOVA_PISNIA_CART_ITEM = new CartItem()
            .setId(2L)
            .setShoppingCart(new ShoppingCart())
            .setBook(LISOVA_PISNIA)
            .setQuantity(10);
    private static final ShoppingCart VALID_SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(Set.of(ZAKHAR_BERKUT_CART_ITEM, LISOVA_PISNIA_CART_ITEM));
    private static final OrderItem ZAKHAR_BERKUT_ORDER_ITEM = new OrderItem()
            .setId(1L)
            .setBook(ZAKHAR_BERKUT)
            .setQuantity(5)
            .setPrice(BigDecimal.valueOf(100));
    private static final OrderItem LISOVA_PISNIA_ORDER_ITEM = new OrderItem()
            .setId(2L)
            .setBook(LISOVA_PISNIA)
            .setQuantity(10)
            .setPrice(BigDecimal.valueOf(100));
    private static final Order VALID_ORDER = new Order()
            .setId(1L)
            .setUser(USER)
            .setStatus(Order.Status.PENDING)
            .setTotal(BigDecimal.valueOf(200))
            .setOrderDate(LocalDateTime.now())
            .setShippingAddress("123 Main Street")
            .setOrderItems(Set.of(ZAKHAR_BERKUT_ORDER_ITEM, LISOVA_PISNIA_ORDER_ITEM));
    private static final OrderItemDto ZAKHAR_BERKUT_ORDER_ITEM_DTO = new OrderItemDto()
            .setId(1L)
            .setBookId(1L)
            .setQuantity(5);
    private static final OrderItemDto LISOVA_PISNIA_ORDER_ITEM_DTO = new OrderItemDto()
            .setId(2L)
            .setBookId(2L)
            .setQuantity(10);
    private static final OrderDto VALID_ORDER_DTO = new OrderDto()
            .setId(1L)
            .setUserId(1L)
            .setOrderItems(List.of(ZAKHAR_BERKUT_ORDER_ITEM_DTO, LISOVA_PISNIA_ORDER_ITEM_DTO))
            .setOrderDate(LocalDateTime.now())
            .setStatus(Order.Status.PENDING);

    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeAll
    static void beforeAll() {
        ZAKHAR_BERKUT_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
        LISOVA_PISNIA_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
        ZAKHAR_BERKUT_ORDER_ITEM.setOrder(VALID_ORDER);
        LISOVA_PISNIA_ORDER_ITEM.setOrder(VALID_ORDER);
    }

    @Test
    @DisplayName("Test create() with a valid shipping address dto")
    void create_withValidShippingAddressDto_returnOrderDto() {
        ShippingAddressDto shippingAddressDto = new ShippingAddressDto()
                .setShippingAddress("123 Main Street");
        when(shoppingCartService.getOrCreateUsersShoppingCart()).thenReturn(VALID_SHOPPING_CART);
        when(cartItemMapper.toOrderItem(ZAKHAR_BERKUT_CART_ITEM))
                .thenReturn(ZAKHAR_BERKUT_ORDER_ITEM);
        when(cartItemMapper.toOrderItem(LISOVA_PISNIA_CART_ITEM))
                .thenReturn(LISOVA_PISNIA_ORDER_ITEM);
        when(orderRepository.save(any())).thenReturn(VALID_ORDER);
        when(orderMapper.toDto(any())).thenReturn(VALID_ORDER_DTO);

        OrderDto actual = orderServiceImpl.create(shippingAddressDto);

        assertEquals(VALID_ORDER_DTO, actual);
    }

    @Test
    @DisplayName("Test findAllByUser() without pagination")
    void findAllByUser_withoutPagination_returnListOfOrderDtos() {
        when(userService.getCurrentUser()).thenReturn(USER);
        when(orderRepository.findAllByUser(any(), any())).thenReturn(List.of(VALID_ORDER));
        when(orderMapper.toDto(any())).thenReturn(VALID_ORDER_DTO);

        List<OrderDto> actual = orderServiceImpl.findAllByUser(Pageable.unpaged());

        assertEquals(List.of(VALID_ORDER_DTO), actual);
    }

    @Test
    @DisplayName("Test updateStatus() with valid id and status")
    void updateStatus_withValidIdAndStatus_returnOrderDto() {
        StatusRequestDto statusRequestDto = new StatusRequestDto()
                .setStatus("PENDING");
        when(orderRepository.findById(any())).thenReturn(Optional.of(VALID_ORDER));
        when(orderItemRepository.getOrderItemsByOrderId(any()))
                .thenReturn(List.of(ZAKHAR_BERKUT_ORDER_ITEM, LISOVA_PISNIA_ORDER_ITEM));
        when(orderMapper.toDto(any())).thenReturn(VALID_ORDER_DTO);
        when(orderItemMapper.toDto(ZAKHAR_BERKUT_ORDER_ITEM))
                .thenReturn(ZAKHAR_BERKUT_ORDER_ITEM_DTO);
        when(orderItemMapper.toDto(LISOVA_PISNIA_ORDER_ITEM))
                .thenReturn(LISOVA_PISNIA_ORDER_ITEM_DTO);

        OrderDto actual = orderServiceImpl.updateStatus(VALID_ORDER.getId(), statusRequestDto);

        assertEquals(VALID_ORDER_DTO, actual);
    }

    @Test
    @DisplayName("Test getOrderItemsByOrderId() with a valid order id")
    void getOrderItemsByOrderId_withValidId_returnListOfOrderItemDtos() {
        when(orderRepository.findByIdAndUser(any(), any())).thenReturn(Optional.of(VALID_ORDER));
        when(orderItemRepository.getOrderItemsByOrderId(any()))
                .thenReturn(List.of(ZAKHAR_BERKUT_ORDER_ITEM, LISOVA_PISNIA_ORDER_ITEM));
        when(orderItemMapper.toDto(ZAKHAR_BERKUT_ORDER_ITEM))
                .thenReturn(ZAKHAR_BERKUT_ORDER_ITEM_DTO);
        when(orderItemMapper.toDto(LISOVA_PISNIA_ORDER_ITEM))
                .thenReturn(LISOVA_PISNIA_ORDER_ITEM_DTO);

        List<OrderItemDto> actual = orderServiceImpl.getOrderItemsByOrderId(VALID_ORDER.getId());

        assertEquals(List.of(ZAKHAR_BERKUT_ORDER_ITEM_DTO, LISOVA_PISNIA_ORDER_ITEM_DTO), actual);
    }

    @Test
    @DisplayName("Test getOrderItemByIdAndOrderId() with valid item id and order id")
    void getOrderItemByIdAndOrderId_withValidIds_returnOrderItemDto() {
        when(orderRepository.findByIdAndUser(any(), any())).thenReturn(Optional.of(VALID_ORDER));
        when(orderItemRepository.getOrderItemsByOrderId(any()))
                .thenReturn(List.of(ZAKHAR_BERKUT_ORDER_ITEM, LISOVA_PISNIA_ORDER_ITEM));
        when(orderItemMapper.toDto(ZAKHAR_BERKUT_ORDER_ITEM))
                .thenReturn(ZAKHAR_BERKUT_ORDER_ITEM_DTO);
        when(orderItemMapper.toDto(LISOVA_PISNIA_ORDER_ITEM))
                .thenReturn(LISOVA_PISNIA_ORDER_ITEM_DTO);

        OrderItemDto actual = orderServiceImpl.getOrderItemByIdAndOrderId(
                ZAKHAR_BERKUT_ORDER_ITEM.getId(),
                VALID_ORDER.getId()
        );

        assertEquals(ZAKHAR_BERKUT_ORDER_ITEM_DTO, actual);
    }
}
