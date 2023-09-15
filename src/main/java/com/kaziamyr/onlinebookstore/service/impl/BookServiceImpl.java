package com.kaziamyr.onlinebookstore.service.impl;

import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.mapper.BookMapper;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import com.kaziamyr.onlinebookstore.repository.CategoryRepository;
import com.kaziamyr.onlinebookstore.service.BookService;
import com.kaziamyr.onlinebookstore.specification.SpecificationProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final SpecificationProvider<Book> specificationProvider;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setCategories(getCategorySet(requestDto));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(book -> bookRepository.findBookById(book.getId()).get())
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookDto) {
        Book updatedBook = bookMapper.toModel(bookDto);
        updatedBook.setId(id);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    private Set<Category> getCategorySet(CreateBookRequestDto requestDto) {
        return Stream.of(requestDto.getCategoryIds())
                .map(categoryRepository::getReferenceById)
                .collect(Collectors.toSet());
    }

    public List<BookDto> getAll(Map<String, String> params) {
        Specification<Book> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            List<String> listOfValues = Arrays.asList(entry.getValue().split(","));
            Specification<Book> sp =
                    specificationProvider.getSpecification(listOfValues, entry.getKey());
            specification = specification.and(sp);
        }
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
