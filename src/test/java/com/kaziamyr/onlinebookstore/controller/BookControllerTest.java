package com.kaziamyr.onlinebookstore.controller;

import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_FICTION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_LESIA_UKRAINKA_FICTION_RELATION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_LISOVA_PISNIA;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ZAKHAR_BERKUT;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.ADD_ZAKHAR_BERKUT_FICTION_RELATION;
import static com.kaziamyr.onlinebookstore.config.SqlFilesPaths.DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES;
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
    protected static MockMvc mockMvc;
    private static final int VALID_BOOK_ID = 1;
    private static final int INVALID_BOOK_ID = 15;
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
                    ADD_FICTION,
                    ADD_LISOVA_PISNIA,
                    ADD_ZAKHAR_BERKUT,
                    ADD_LESIA_UKRAINKA_FICTION_RELATION,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findAll() without pagination")
    void findAll_withoutPagination_returnListOfBookDtos() throws Exception {
        List<BookDto> expected = List.of(ZAKHAR_BERKUT_DTO, LISOVA_PISNIA_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/books")).andReturn();

        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> assertTrue(EqualsBuilder
                        .reflectionEquals(expected.get(id), actual.get(id), "id")));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getById with a correct id")
    void getById_validId_returnBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/" + VALID_BOOK_ID)).andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(ZAKHAR_BERKUT_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Test getById with an invalid id")
    void getById_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/books/" + INVALID_BOOK_ID))
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_FICTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() with a correct book")
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
            scripts = ADD_FICTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() with an invalid book")
    void create_withInvalidBook_throwException() throws Exception {
        CreateBookRequestDto invalidZakharBerkutRequest = new CreateBookRequestDto()
                .setTitle("Zakhar Berkut")
                .setAuthor("Ivan Franko")
                .setIsbn("97883391238674")
                .setPrice(BigDecimal.valueOf(10))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(new Long[]{1L});
        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(invalidZakharBerkutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
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
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with correct id and request dto")
    void update_validIdAndRequestDto_returnBookDto() throws Exception {
        CreateBookRequestDto lisovaPisniaRequest = new CreateBookRequestDto()
                .setTitle("Lisova Pisnia")
                .setAuthor("Lesia Ukrainka")
                .setIsbn("9783769736526")
                .setPrice(BigDecimal.valueOf(12))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(new Long[]{1L});
        MvcResult mvcResult = mockMvc.perform(put("/books/" + VALID_BOOK_ID)
                        .content(objectMapper.writeValueAsString(lisovaPisniaRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(LISOVA_PISNIA_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_ZAKHAR_BERKUT,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with incorrect id and request dto")
    void update_invalidIdAndRequestDto_throwException() throws Exception {
        CreateBookRequestDto invalidLisovaPisniaRequest = new CreateBookRequestDto()
                .setTitle("Lisova Pisnia")
                .setAuthor("Lesia Ukrainka")
                .setIsbn("97837697365266")
                .setPrice(BigDecimal.valueOf(12))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg")
                .setCategoryIds(new Long[]{1L});
        mockMvc.perform(put("/books/" + INVALID_BOOK_ID)
                        .content(objectMapper.writeValueAsString(invalidLisovaPisniaRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION,
                    ADD_LISOVA_PISNIA,
                    ADD_ZAKHAR_BERKUT,
                    ADD_LESIA_UKRAINKA_FICTION_RELATION,
                    ADD_ZAKHAR_BERKUT_FICTION_RELATION
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test search() without search parameters")
    void search_noSearchParameters_returnListOfBookDtos() throws Exception {
        List<BookDto> expected = List.of(ZAKHAR_BERKUT_DTO, LISOVA_PISNIA_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/books/search")).andReturn();

        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> assertTrue(EqualsBuilder
                        .reflectionEquals(expected.get(id), actual.get(id), "id")));
    }
}
