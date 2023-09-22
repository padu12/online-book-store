package com.kaziamyr.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import com.kaziamyr.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

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
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(
            scripts = {
                    "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
                    "classpath:database/add-shopping-cart-to-shopping_carts-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findShoppingCartByUser() with a valid user")
    void findShoppingCartByUser_validUser_returnOptionalOfShoppingCart() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findShoppingCartByUser(USER);

        assertEquals(Optional.of(VALID_SHOPPING_CART), actual);
    }
}