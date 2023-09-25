package com.kaziamyr.onlinebookstore.repository;

import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_FICTION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_LESIA_UKRAINKA_FICTION_RELATION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_LISOVA_PISNIA;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ZAKHAR_BERKUT;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ZAKHAR_BERKUT_FICTION_RELATION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final Long ZAKHAR_BERKUT_ID = 1L;
    private static final Long FICTION_ID = 1L;
    private static final Book ZAKHAR_BERKUT = new Book()
            .setId(1L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9788339123867")
            .setPrice(BigDecimal.valueOf(10))
            .setDescription("description")
            .setCoverImage("https://example.com/updated-cover-image.jpg")
            .setCategories(Set.of(new Category()
                    .setId(1L)
                    .setName("Fiction")
                    .setDescription("Fiction books")));
    private static final Long INVALID_BOOK_ID = 10L;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test findBookById() with a valid id")
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBookById_validId_returnOptionalOfBook() {
        @SuppressWarnings("OptionalGetWithoutIsPresent") Book actual = bookRepository.findBookById(ZAKHAR_BERKUT_ID).get();

        assertEquals(ZAKHAR_BERKUT, actual);
    }

    @Test
    @DisplayName("Test findBookById() with an invalid id")
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBookById_invalidId_returnEmptyOptional() {
        assertTrue(bookRepository.findBookById(INVALID_BOOK_ID).isEmpty());
    }

    @Test
    @DisplayName("Test findBookById() with two books expected")
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION,
                    ADD_LISOVA_PISNIA,
                    ADD_LESIA_UKRAINKA_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_validId_returnListOfBooks() {
        Book lisovaPisnia = new Book()
                .setId(2L)
                .setTitle("Lisova Pisnia")
                .setAuthor("Lesia Ukrainka")
                .setIsbn("9783769736526")
                .setPrice(BigDecimal.valueOf(12))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategories(Set.of(new Category()
                        .setId(1L)
                        .setName("Fiction")
                        .setDescription("Fiction books")));
        List<Book> expected = List.of(ZAKHAR_BERKUT, lisovaPisnia);

        List<Book> actual = bookRepository.findAllByCategoryId(FICTION_ID);

        assertEquals(expected, actual);
    }
}
