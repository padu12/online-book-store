package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import java.util.Map;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    List<BookDto> getAll(Map<String, String> params);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookDto);
}
