package com.kaziamyr.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.category.CategoryDto;
import com.kaziamyr.onlinebookstore.dto.category.SaveCategoryRequestDto;
import java.math.BigDecimal;
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
class CategoryControllerTest {
    private static final CategoryDto FANTASY_DTO = new CategoryDto()
            .setId(2L)
            .setName("Fantasy")
            .setDescription("Fantasy books");
    private static final SaveCategoryRequestDto FANTASY_REQUEST_DTO = new SaveCategoryRequestDto()
            .setName("Fantasy")
            .setDescription("Fantasy books");
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() with a valid request body")
    void create_validRequestBody_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql",
                    "classpath:database/categories/add-fantasy-to-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getAll() without pagination")
    void getAll_withoutPagination_returnListOfCategoryDtos() throws Exception {
        CategoryDto fictionDto = new CategoryDto()
                .setId(1L)
                .setName("Fiction")
                .setDescription("Fiction books");
        List<CategoryDto> expected = List.of(fictionDto, FANTASY_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/categories")).andReturn();

        List<CategoryDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class));

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
                    "classpath:database/categories/add-fantasy-to-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getCategoryById() with a correct id")
    void getCategoryById_validId_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/2")).andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with valid id and request dto")
    void update_validIdAndRequestDto_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/categories/1")
                        .content(objectMapper.writeValueAsString(FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:database/categories/add-fiction-to-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test delete() with a valid id")
    void delete_validId_returnNothing() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
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
    @DisplayName("Test getBooksByCategoryId() with valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtosWithoutCategoryId() throws Exception {
        BookDtoWithoutCategoryIds zakharBerkutDto = new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Zakhar Berkut")
                .setAuthor("Ivan Franko")
                .setIsbn("9788339123867")
                .setPrice(BigDecimal.valueOf(10))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg");
        BookDtoWithoutCategoryIds lisovaPisniaDto = new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Lisova Pisnia")
                .setAuthor("Lesia Ukrainka")
                .setIsbn("9783769736526")
                .setPrice(BigDecimal.valueOf(12))
                .setDescription("description")
                .setCoverImage("https://example.com/updated-cover-image.jpg");
        List<BookDtoWithoutCategoryIds> expected = List.of(lisovaPisniaDto, zakharBerkutDto);
        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books")).andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> {
                    assertTrue(EqualsBuilder
                            .reflectionEquals(expected.get(id), actual.get(id), "id"));
                });
    }
}
