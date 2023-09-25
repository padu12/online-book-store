package com.kaziamyr.onlinebookstore.repository;

import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ORDER;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.DELETE_ORDER_RELATED_TABLES;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.OrderItem;
import com.kaziamyr.onlinebookstore.model.Role;
import com.kaziamyr.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
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

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll() {
        ZAKHAR_BERKUT_ORDER_ITEM.setOrder(VALID_ORDER);
        LISOVA_PISNIA_ORDER_ITEM.setOrder(VALID_ORDER);
    }

    @Test
    @Sql(
            scripts = {
                    DELETE_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findAllByUser() with a valid user")
    void findAllByUser_validUser_returnListOfOrders() {
        List<Order> actual = orderRepository.findAllByUser(Pageable.unpaged(), USER);

        assertEquals(List.of(VALID_ORDER), actual);
    }

    @Test
    @Sql(
            scripts = {
                    DELETE_ORDER_RELATED_TABLES,
                    ADD_ORDER
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_ORDER_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findByIdAndUser() with valid id and user")
    void findByIdAndUser_validIdAndUser_returnOptionalOfOrder() {
        Optional<Order> actual = orderRepository.findByIdAndUser(VALID_ORDER.getId(), USER);

        assertEquals(Optional.of(VALID_ORDER), actual);
    }
}
