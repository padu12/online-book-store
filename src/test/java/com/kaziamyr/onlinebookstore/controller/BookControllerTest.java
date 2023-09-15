package com.kaziamyr.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
class BookControllerTest {
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
    private static final BookDto ZAKHAR_BERKUT_DTO = new BookDto()
            .setId(1L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9788339123867")
            .setPrice(BigDecimal.valueOf(10))
            .setDescription("description")
            .setCoverImage("https://example.com/updated-cover-image.jpg")
            .setCategories(List.of(1L));
    private static final CreateBookRequestDto ZAKHAR_BERKUT_REQUEST = new CreateBookRequestDto()
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9788339123867")
            .setPrice(BigDecimal.valueOf(10))
            .setDescription("description")
            .setCoverImage("https://example.com/updated-cover-image.jpg")
            .setCategoryIds(new Long[]{1L});
    private static final Book LISOVA_PISNIA = new Book()
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
    private static final BookDto LISOVA_PISNIA_DTO = new BookDto()
            .setId(2L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9783769736526")
            .setPrice(BigDecimal.valueOf(12))
            .setDescription("description")
            .setCoverImage("https://example.com/updated-cover-image.jpg")
            .setCategories(List.of(1L));
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Test findAll() without pagination")
    void findAll_withoutPagination_returnListOfBooks() throws Exception {
        List<Book> expected = List.of(ZAKHAR_BERKUT, LISOVA_PISNIA);

        MvcResult actual = mockMvc.perform(get("/books"))
                .andReturn();
    }

    @Test
    void getById() {
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-fiction-to-categories-table.sql"
            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql"
            , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() without pagination")
    void create_withValidBook_returnBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(ZAKHAR_BERKUT_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_DTO, actual);
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }

    @Test
    void searchBooks() {
    }
}