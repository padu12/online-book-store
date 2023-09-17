package com.kaziamyr.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test findBookById() with a valid id")
    @Sql(scripts = {
            "classpath:database/categories/add-fiction-to-categories-table.sql",
            "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
            "classpath:database/books_categories"
                    + "/add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/"
            + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBookById_validId_returnOptionalOfBook() {
        Book actual = bookRepository.findBookById(ZAKHAR_BERKUT_ID).get();

        assertEquals(ZAKHAR_BERKUT, actual);
    }

    @Test
    @DisplayName("Test findBookById() with two books expected")
    @Sql(scripts = {
            "classpath:database/categories/add-fiction-to-categories-table.sql",
            "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
            "classpath:database/books_categories"
                    + "/add-zakhar-berkut-fiction-relation-to-books_categories-table.sql",
            "classpath:database/books/add-lisova-pisnia-to-books-table.sql",
            "classpath:database/books_categories"
                    + "/add-lesia-ukrainka-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/"
            + "delete-all-from-categories-books-books_categories-tables.sql",
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
