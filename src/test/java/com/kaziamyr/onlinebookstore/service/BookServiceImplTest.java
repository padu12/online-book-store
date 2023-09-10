package com.kaziamyr.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.mapper.BookMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CategoryRepository;
import com.kaziamyr.onlinebookstore.service.impl.BookServiceImpl;
import com.kaziamyr.onlinebookstore.specification.SpecificationProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
    private static final Book LISOVA_PISNIA = new Book()
            .setId(1L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setIsbn("9787816949778")
            .setPrice(new BigDecimal("13.50"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(Set.of(
                    new Category()
                            .setId(1L)
                            .setName("Fantasy")
            ));
    private static final BookDto LISOVA_PISNIA_DTO = new BookDto()
            .setId(1L)
            .setTitle("Lisova Pisnia")
            .setAuthor("Lesia Ukrainka")
            .setCategories(List.of("Fantasy"))
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
            .setCategories(Set.of(
                    new Category()
                            .setId(1L)
                            .setName("Fantasy")
            ));
    private static final BookDto ZAKHAR_BERKUT_DTO = new BookDto()
            .setId(2L)
            .setTitle("Zakhar Berkut")
            .setAuthor("Ivan Franko")
            .setIsbn("9781691430703")
            .setPrice(new BigDecimal("19.99"))
            .setDescription("It's a cool book")
            .setCoverImage("https://www.image.com")
            .setCategories(List.of("Fantasy"));

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private SpecificationProvider<Book> specificationProvider;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeEach
    void setUp() {

    }

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

        EntityNotFoundException actual =
                assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.getBookById(16L));

        assertEquals("Can't find book with id 16", actual.getMessage());
    }

    @Test
    @DisplayName("Test findAll()")
    public void findAll_withoutPageable_shouldReturnList() {
        List<Book> books = List.of(LISOVA_PISNIA, ZAKHAR_BERKUT);
        List<BookDto> expected = List.of(LISOVA_PISNIA_DTO, ZAKHAR_BERKUT_DTO);
        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(books));
        when(bookRepository.findBookById(1L)).thenReturn(Optional.of(LISOVA_PISNIA));
        when(bookRepository.findBookById(2L)).thenReturn(Optional.of(ZAKHAR_BERKUT));
        when(bookMapper.toDto(LISOVA_PISNIA)).thenReturn(LISOVA_PISNIA_DTO);
        when(bookMapper.toDto(ZAKHAR_BERKUT)).thenReturn(ZAKHAR_BERKUT_DTO);

        List<BookDto> actual = bookServiceImpl.findAll(Pageable.unpaged());

        assertEquals(expected, actual);
    }
}
