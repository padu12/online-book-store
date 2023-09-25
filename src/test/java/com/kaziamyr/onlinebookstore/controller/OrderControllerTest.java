package com.kaziamyr.onlinebookstore.controller;

import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ORDER;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_SHOPPING_CART;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaziamyr.onlinebookstore.dto.OrderDto;
import com.kaziamyr.onlinebookstore.dto.OrderItemDto;
import com.kaziamyr.onlinebookstore.dto.ShippingAddressDto;
import com.kaziamyr.onlinebookstore.dto.StatusRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    protected static MockMvc mockMvc;
    private static final Long INVALID_ORDER_ID = 10L;
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
            .setTotal(BigDecimal.valueOf(220))
            .setOrderDate(LocalDateTime.of(2021, 12, 1, 14, 30, 15))
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
            .setOrderDate(LocalDateTime.of(2021, 12, 1, 14, 30, 15))
            .setTotal(BigDecimal.valueOf(220))
            .setStatus(Order.Status.PENDING);
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        ZAKHAR_BERKUT_ORDER_ITEM.setOrder(VALID_ORDER);
        LISOVA_PISNIA_ORDER_ITEM.setOrder(VALID_ORDER);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_SHOPPING_CART
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test place() with a valid user")
    void place_validUser_returnOrderDto() throws Exception {
        ShippingAddressDto shippingAddressDto = new ShippingAddressDto()
                .setShippingAddress("123 Main Street");
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(shippingAddressDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        OrderDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(
                VALID_ORDER_DTO,
                actual,
                "id",
                "orderDate",
                "orderItems"
        ));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test retrieveUsersHistory() with a valid user")
    void retrieveUsersHistory_validUser_returnListOfOrderDtos() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders")).andReturn();

        List<OrderDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), OrderDto[].class
        ));

        assertTrue(EqualsBuilder.reflectionEquals(
                VALID_ORDER_DTO,
                actual.get(0),
                "id",
                "orderDate",
                "orderItems"
        ));
    }

    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test updateStatus() with valid id and status")
    void updateStatus_withValidIdAndStatus_returnOrderDto() throws Exception {
        StatusRequestDto statusRequestDto = new StatusRequestDto()
                .setStatus("PENDING");
        MvcResult mvcResult = mockMvc.perform(patch("/orders/" + VALID_ORDER.getId())
                        .content(objectMapper.writeValueAsString(statusRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        OrderDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(
                VALID_ORDER_DTO,
                actual,
                "id",
                "orderDate",
                "orderItems"
        ));
    }

    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test updateStatus() with invalid id and status")
    void updateStatus_withInvalidIdAndStatus_throwException() throws Exception {
        StatusRequestDto statusRequestDto = new StatusRequestDto()
                .setStatus("PENDING");
        mockMvc.perform(patch("/orders/" + INVALID_ORDER_ID)
                        .content(objectMapper.writeValueAsString(statusRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getAllByOrderId() with a valid id")
    void getAllByOrderId_validId_returnListOfOrderItemDtos() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(get("/orders/" + VALID_ORDER.getId() + "/items")).andReturn();

        List<OrderItemDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), OrderItemDto[].class
        ));

        IntStream.range(0, VALID_ORDER_DTO.getOrderItems().size())
                .forEach(id -> assertTrue(EqualsBuilder
                        .reflectionEquals(VALID_ORDER_DTO.getOrderItems().get(id), actual.get(id),
                                "id")));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getAllByOrderId() with an invalid id")
    void getAllByOrderId_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/orders/" + INVALID_ORDER_ID + "/items"))
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getByIdAndItemId with valid order id and item id")
    void getByIdAndItemId_validId_returnOrderItemDto() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(get("/orders/"
                        + VALID_ORDER.getId()
                        + "/items/"
                        + ZAKHAR_BERKUT_ORDER_ITEM_DTO.getId()
                )).andReturn();

        OrderItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderItemDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_ORDER_ITEM_DTO, actual, "id"));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getByIdAndItemId with invalid order id and item id")
    void getByIdAndItemId_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/orders/"
                + INVALID_ORDER_ID
                + "/items/"
                + ZAKHAR_BERKUT_ORDER_ITEM_DTO.getId()
        )).andExpect(status().isConflict());
    }
}
