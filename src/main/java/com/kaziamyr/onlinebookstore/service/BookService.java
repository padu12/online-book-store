package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.BookDto;
import com.kaziamyr.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> getAll(Map<String, String> params);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookDto);
}
