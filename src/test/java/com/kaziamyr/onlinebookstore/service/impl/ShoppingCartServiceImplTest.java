package com.kaziamyr.onlinebookstore.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kaziamyr.onlinebookstore.dto.cartitem.CartItemWithBookTitleDto;
import com.kaziamyr.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.kaziamyr.onlinebookstore.mapper.CartItemMapper;
import com.kaziamyr.onlinebookstore.mapper.ShoppingCartMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CartItemRepository;
import com.kaziamyr.onlinebookstore.repository.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
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
            .setPrice(new BigDecimal("19.99"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(FANTASY));
    private static final Book LISOVA_PISNIA = new Book()
            .setId(2L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("13.50"))
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
    private static final CartItemWithBookTitleDto ZAKHAR_BERKUT_CART_ITEM_DTO =
            new CartItemWithBookTitleDto()
                    .setId(1L)
                    .setBookId(1L)
                    .setBookTitle("Zakhar Berkut")
                    .setQuantity(5);
    private static final CartItemWithBookTitleDto LISOVA_PISNIA_CART_ITEM_DTO =
            new CartItemWithBookTitleDto()
                    .setId(2L)
                    .setBookId(2L)
                    .setBookTitle("Lisova Pisnia")
                    .setQuantity(10);
    private static final ShoppingCartDto VALID_SHOPPING_CART_DTO = new ShoppingCartDto()
            .setId(1L)
            .setUserId(1L)
            .setCartItems(List.of(ZAKHAR_BERKUT_CART_ITEM_DTO, LISOVA_PISNIA_CART_ITEM_DTO));

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @BeforeAll
    static void beforeAll() {
        ZAKHAR_BERKUT_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
        LISOVA_PISNIA_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);
    }

    @Test
    @DisplayName("Test getByUser() with a valid user")
    void getByUser_validUser_returnShoppingCartDto() {
        when(userService.getCurrentUser()).thenReturn(USER);
        when(shoppingCartRepository.findShoppingCartByUser(any())).thenReturn(
                Optional.of(VALID_SHOPPING_CART));
        when(cartItemRepository.findAllByShoppingCart(any())).thenReturn(
                List.of(ZAKHAR_BERKUT_CART_ITEM, LISOVA_PISNIA_CART_ITEM));
        when(shoppingCartMapper.toDto(any())).thenReturn(VALID_SHOPPING_CART_DTO);

        ShoppingCartDto actual = shoppingCartServiceImpl.getByUser();

        Assertions.assertEquals(VALID_SHOPPING_CART_DTO, actual);
    }

    @Test
    void addBookToShoppingCart() {
    }

    @Test
    void updateBookInShoppingCart() {
    }

    @Test
    void deleteBookFromShoppingCartById() {
    }

    @Test
    void getOrCreateUsersShoppingCart() {
    }
}
