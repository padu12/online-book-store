package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.BookDto;
import com.kaziamyr.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookDto);
}
