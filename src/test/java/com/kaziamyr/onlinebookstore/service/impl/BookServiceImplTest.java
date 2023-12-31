package com.kaziamyr.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.mapper.BookMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CategoryRepository;
import com.kaziamyr.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
public class BookServiceImplTest {
    private static final Category CATEGORY = new Category()
            .setId(1L)
            .setName("Fantasy");

    private static final CreateBookRequestDto CREATE_LISOVA_PISNIA = new CreateBookRequestDto()
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("13.50"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategoryIds(new Long[]{1L});
    private static final Book LISOVA_PISNIA = new Book()
            .setId(1L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("13.50"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(CATEGORY));
    private static final BookDto LISOVA_PISNIA_DTO = new BookDto()
            .setId(1L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setCategories(List.of(1L))
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("13.50"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com");
    private static final Book ZAKHAR_BERKUT = new Book()
            .setId(2L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9781691430703")
            .setPrice(new BigDecimal("19.99"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(CATEGORY));
    private static final BookDto ZAKHAR_BERKUT_DTO = new BookDto()
            .setId(2L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9781691430703")
            .setPrice(new BigDecimal("19.99"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(List.of(1L));
    private static final Long INVALID_BOOK_ID = 16L;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Test getBookById() with a valid input id")
    public void getBookById_withValidBookId_shouldReturnBookDto() {
        when(bookRepository.findBookById(anyLong())).thenReturn(Optional.of(LISOVA_PISNIA));
        when(bookMapper.toDto(LISOVA_PISNIA)).thenReturn(LISOVA_PISNIA_DTO);

        BookDto actual = bookServiceImpl.getBookById(LISOVA_PISNIA.getId());

        assertEquals(LISOVA_PISNIA_DTO, actual);
    }

    @Test
    @DisplayName("Test getBookById() with an invalid input id")
    public void getBookById_withNotValidId_throwException() {
        when(bookRepository.findBookById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getBookById(INVALID_BOOK_ID));

        assertEquals("Can't find book with id " + INVALID_BOOK_ID, actual.getMessage());
    }

    @Test
    @DisplayName("Test findAll() without pagination")
    public void findAll_withoutPageable_shouldReturnList() {
        List<Book> books = List.of(LISOVA_PISNIA, ZAKHAR_BERKUT);
        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(books));
        when(bookRepository.findBookById(1L)).thenReturn(Optional.of(LISOVA_PISNIA));
        when(bookRepository.findBookById(2L)).thenReturn(Optional.of(ZAKHAR_BERKUT));
        when(bookMapper.toDto(LISOVA_PISNIA)).thenReturn(LISOVA_PISNIA_DTO);
        when(bookMapper.toDto(ZAKHAR_BERKUT)).thenReturn(ZAKHAR_BERKUT_DTO);

        List<BookDto> expected = List.of(LISOVA_PISNIA_DTO, ZAKHAR_BERKUT_DTO);
        List<BookDto> actual = bookServiceImpl.findAll(Pageable.unpaged());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save() with a valid book entity")
    public void save_validBook_shouldReturnBookDto() {
        when(bookMapper.toModel(any())).thenReturn(LISOVA_PISNIA);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(CATEGORY));
        when(bookRepository.save(any())).thenReturn(LISOVA_PISNIA);
        when(bookMapper.toDto(any())).thenReturn(LISOVA_PISNIA_DTO);

        BookDto actual = bookServiceImpl.save(CREATE_LISOVA_PISNIA);

        assertEquals(LISOVA_PISNIA_DTO, actual);
    }

    @Test
    @DisplayName("Test save() with an invalid book entity")
    public void save_invalidBook_throwException() {
        CreateBookRequestDto createInvalidLisovaPisnia =
                new CreateBookRequestDto()
                        .setTitle("Lisova Pisnia")
                        .setAuthor("Lesia Ukrainka")
                        .setIsbn("978781694977871")
                        .setPrice(new BigDecimal("13.50"))
                        .setDescription("It's a cool book")
                        .setCoverImage("https://www.image.com")
                        .setCategoryIds(new Long[]{1L});
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.save(createInvalidLisovaPisnia));

        assertEquals("Can't find category with id 1", actual.getMessage());
    }

    @Test
    @DisplayName("Test deleteById() with a correct id")
    public void delete_validId_returnNothing() {
        assertDoesNotThrow(() -> bookServiceImpl.deleteById(LISOVA_PISNIA.getId()));
    }

    @Test
    @DisplayName("Test updateById() with a valid id")
    public void updateById_validId_returnBookDto() {
        when(bookMapper.toModel(any())).thenReturn(LISOVA_PISNIA);
        when(bookRepository.save(any())).thenReturn(LISOVA_PISNIA);
        when(bookMapper.toDto(any())).thenReturn(LISOVA_PISNIA_DTO);

        BookDto actual = bookServiceImpl.updateById(LISOVA_PISNIA.getId(), CREATE_LISOVA_PISNIA);

        assertEquals(LISOVA_PISNIA_DTO, actual);
    }
}
