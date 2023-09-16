package com.kaziamyr.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
class BookControllerTest {
    private static final BookDto ZAKHAR_BERKUT_DTO = new BookDto()
            .setId(1L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9788339123867")
            .setPrice(BigDecimal.valueOf(10))
            .setDescription("description")
            .setCoverImage("https://example.com/updated-cover-image.jpg")
            .setCategories(List.of(1L));
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
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/books/add-lisova-pisnia-to-books-table.sql",
                    "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
                    "classpath:database/books_categories/"
                            + "add-lesia-ukrainka-fiction-relation-to-books_categories-table.sql",
                    "classpath:database/books_categories/"
                            + "add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findAll() without pagination")
    void findAll_withoutPagination_returnListOfBookDtos() throws Exception {
        List<BookDto> expected = List.of(ZAKHAR_BERKUT_DTO, LISOVA_PISNIA_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/books")).andReturn();

        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> {
                    assertTrue(EqualsBuilder
                            .reflectionEquals(expected.get(id), actual.get(id), "id"));
                });
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
                    "classpath:database/books_categories/"
                            + "add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getById with a correct id")
    void getById_validId_returnBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/1")).andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-fiction-to-categories-table.sql"
            , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() without pagination")
    void create_withValidBook_returnBookDto() throws Exception {
        CreateBookRequestDto zakharBerkutRequest = new CreateBookRequestDto()
                .setTitle("Zakhar Berkut")
                .setAuthor("Ivan Franko")
                .setIsbn("9788339123867")
                .setPrice(BigDecimal.valueOf(10))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(new Long[]{1L});
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(zakharBerkutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
                    "classpath:database/books_categories/"
                            + "add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test deleteById() with a valid id")
    void deleteById_validId_returnNothing() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
                    "classpath:database/books_categories/"
                            + "add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("test update() with correct id and request dto")
    void update_validIdAndRequestDto_returnBookDto() throws Exception {
        CreateBookRequestDto lisovaPisniaRequest = new CreateBookRequestDto()
                .setTitle("Lisova Pisnia")
                .setAuthor("Lesia Ukrainka")
                .setIsbn("9783769736526")
                .setPrice(BigDecimal.valueOf(12))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(new Long[]{1L});
        MvcResult mvcResult = mockMvc.perform(put("/books/1")
                        .content(objectMapper.writeValueAsString(lisovaPisniaRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(LISOVA_PISNIA_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/books/add-lisova-pisnia-to-books-table.sql",
                    "classpath:database/books/add-zakhar-berkut-to-books-table.sql",
                    "classpath:database/books_categories/"
                            + "add-lesia-ukrainka-fiction-relation-to-books_categories-table.sql",
                    "classpath:database/books_categories/"
                            + "add-zakhar-berkut-fiction-relation-to-books_categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test search() without search parameters")
    void search_noSearchParameters_returnListOfBookDtos() throws Exception {
        List<BookDto> expected = List.of(ZAKHAR_BERKUT_DTO, LISOVA_PISNIA_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/books/search")).andReturn();

        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> {
                    assertTrue(EqualsBuilder
                            .reflectionEquals(expected.get(id), actual.get(id), "id"));
                });
    }
}
