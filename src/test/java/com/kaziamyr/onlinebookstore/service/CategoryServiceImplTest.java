package com.kaziamyr.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.category.CategoryDto;
import com.kaziamyr.onlinebookstore.dto.category.SaveCategoryRequestDto;
import com.kaziamyr.onlinebookstore.mapper.BookMapper;
import com.kaziamyr.onlinebookstore.mapper.CategoryMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CategoryRepository;
import com.kaziamyr.onlinebookstore.service.impl.CategoryServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    private static final Category FANTASY = new Category()
            .setId(1L)
            .setName("Fantasy");
    private static final CategoryDto FANTASY_DTO = new CategoryDto()
            .setId(1L)
            .setName("Fantasy");

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    @DisplayName("Test findAll() without pagination")
    public void findAll_unpaged_returnListOfCategoryDtos() {
        when(categoryRepository.findAll(Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(FANTASY)));
        when(categoryMapper.toDto(any())).thenReturn(FANTASY_DTO);

        List<CategoryDto> actual = categoryServiceImpl.findAll(Pageable.unpaged());

        assertEquals(List.of(FANTASY_DTO), actual);
    }

    @Test
    @DisplayName("Test findAllBooksByCategoryId() with a valid id")
    public void findAllBooksByCategoryId_validId_returnBookDtosWithoutCategoryIds() {
        Book book = new Book()
                .setId(2L)
                .setTitle("Zakhar Berkut")
                .setAuthor("Ivan Franko")
                .setIsbn("9781691430703")
                .setPrice(new BigDecimal("19.99"))
                .setDescription("It's a cool book")
                .setCoverImage("https://www.image.com")
                .setCategories(Set.of(FANTASY));
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Zakhar Berkut")
                .setAuthor("Ivan Franko")
                .setIsbn("9781691430703")
                .setPrice(new BigDecimal("19.99"))
                .setDescription("It's a cool book")
                .setCoverImage("https://www.image.com");
        when(bookRepository.findAllByCategoryId(anyLong())).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(any())).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = categoryServiceImpl
                .findAllBooksByCategoryId(FANTASY.getId());

        assertEquals(List.of(bookDtoWithoutCategoryIds), actual);
    }

    @Test
    @DisplayName("Test getById() with a valid id")
    public void getById_validId_returnCategoryDto() {
        when(categoryRepository.getReferenceById(anyLong())).thenReturn(FANTASY);
        when(categoryMapper.toDto(any())).thenReturn(FANTASY_DTO);

        CategoryDto actual = categoryServiceImpl.getById(FANTASY_DTO.getId());

        assertEquals(FANTASY_DTO, actual);
    }

    @Test
    @DisplayName("Test save() with a correct request")
    public void save_validRequest_returnCategoryDto() {
        SaveCategoryRequestDto saveCategoryRequestDto = new SaveCategoryRequestDto()
                .setName("Fantasy");
        when(categoryMapper.toEntity(any())).thenReturn(FANTASY);
        when(categoryRepository.save(any())).thenReturn(FANTASY);
        when(categoryMapper.toDto(any())).thenReturn(FANTASY_DTO);

        CategoryDto actual = categoryServiceImpl.save(saveCategoryRequestDto);

        assertEquals(FANTASY_DTO, actual);
    }

    @Test
    @DisplayName("Test update() with valid id and request")
    public void update_validIdAndRequest_returnCategoryDto() {
        SaveCategoryRequestDto updateCategoryRequestDto = new SaveCategoryRequestDto()
                .setName("Fiction");
        Category fiction = new Category()
                .setName("Fiction");
        CategoryDto fictionDto = new CategoryDto()
                .setId(1L)
                .setName("Fiction");
        when(categoryMapper.toEntity(any())).thenReturn(fiction);
        when(categoryRepository.save(any())).thenReturn(fiction);
        when(categoryMapper.toDto(any())).thenReturn(fictionDto);

        CategoryDto actual =
                categoryServiceImpl.update(FANTASY_DTO.getId(), updateCategoryRequestDto);

        assertEquals(fictionDto, actual);
    }

    @Test
    @DisplayName("Test deleteById with correct id")
    public void deleteById_validId_returnNothing() {
        assertDoesNotThrow(() -> categoryServiceImpl.deleteById(FANTASY.getId()));
    }
}
