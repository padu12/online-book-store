package com.kaziamyr.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.cartitem.PutCartItemRequestDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
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
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final Long VALID_CART_ITEM_ID = 2L;
    private static final Long INVALID_CART_ITEM_ID = 10L;
    private static final Long VALID_BOOK_ID = 1L;
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
            .setDescription("Its a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(FANTASY));
    private static final Book LISOVA_PISNIA = new Book()
            .setId(2L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("12"))
            .setDescription("Its a cool book")
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
    private static final CartItemWithBookTitleDto ZAKHAR_BERKUT_CART_ITEM_WITH_TITLE_DTO =
            new CartItemWithBookTitleDto()
                    .setId(1L)
                    .setBookId(1L)
                    .setBookTitle("Zakhar Berkut")
                    .setQuantity(5);
    private static final CartItemWithBookTitleDto LISOVA_PISNIA_CART_ITEM_WITH_TITLE_DTO =
            new CartItemWithBookTitleDto()
                    .setId(2L)
                    .setBookId(2L)
                    .setBookTitle("Lisova Pisnia")
                    .setQuantity(10);
    private static final ShoppingCartDto VALID_SHOPPING_CART_DTO = new ShoppingCartDto()
            .setId(1L)
            .setUserId(1L)
            .setCartItems(List.of(ZAKHAR_BERKUT_CART_ITEM_WITH_TITLE_DTO,
                    LISOVA_PISNIA_CART_ITEM_WITH_TITLE_DTO));
    private static final CartItemDto ZAKHAR_BERKUT_CART_ITEM_DTO = new CartItemDto()
            .setId(1L)
            .setBookId(1L)
            .setQuantity(5);
    private static final PutCartItemRequestDto PUT_CART_ITEM_REQUEST_DTO =
            new PutCartItemRequestDto().setQuantity(15);
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        ZAKHAR_BERKUT_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
        LISOVA_PISNIA_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/add-shopping-cart-to-shopping_carts-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getUsersShoppingCart() with a valid user")
    void getUsersShoppingCart_withValidUser_returnShoppingCartDto() {
        MvcResult mvcResult = mockMvc.perform(get("/cart")).andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(VALID_SHOPPING_CART_DTO, actual, "id"));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/"
                            + "add-empty-shopping-cart-to-shopping_carts_table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test addBookToShoppingCart() with a valid request dto")
    void addBookToShoppingCart_withValidRequestDto_returnCartItemDto() throws Exception {
        CreateCartItemRequestDto cartItemRequestDto = new CreateCartItemRequestDto()
                .setBookId(1L)
                .setQuantity(5);
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(cartItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CartItemDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_CART_ITEM_DTO, actual, "id"));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/"
                            + "add-empty-shopping-cart-to-shopping_carts_table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test addBookToShoppingCart() with an invalid request dto")
    void addBookToShoppingCart_withInvalidRequestDto_returnCartItemDto() throws Exception {
        CreateCartItemRequestDto invalidCartItemRequestDto = new CreateCartItemRequestDto()
                .setBookId(5L)
                .setQuantity(5);
        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(invalidCartItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/add-shopping-cart-to-shopping_carts-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test updateBooksInShoppingCart() with valid id and request dto")
    void updateBooksInShoppingCart_withValidIdAndRequestDto_returnCartItemDto() throws Exception {
        CartItemDto expected = new CartItemDto()
                .setId(2L)
                .setBookId(2L)
                .setQuantity(15);
        MvcResult mvcResult = mockMvc.perform(put("/cart/cart-items/" + VALID_CART_ITEM_ID)
                        .content(objectMapper.writeValueAsString(PUT_CART_ITEM_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CartItemDto.class
        );

        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/add-shopping-cart-to-shopping_carts-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test updateBooksInShoppingCart() with invalid id and request dto")
    void updateBooksInShoppingCart_withInvalidIdAndRequestDto_returnCartItemDto() throws Exception {
        mockMvc.perform(put("/cart/cart-items/" + INVALID_CART_ITEM_ID)
                        .content(objectMapper.writeValueAsString(PUT_CART_ITEM_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/shoppingcart/add-shopping-cart-to-shopping_carts-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test deleteBookFromShoppingCart() with a valid id")
    void deleteBookFromShoppingCart_withValidId_returnNothing() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/" + VALID_BOOK_ID))
                .andExpect(status().isOk());
    }
}
