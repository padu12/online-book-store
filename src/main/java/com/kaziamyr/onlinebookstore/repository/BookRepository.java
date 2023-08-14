package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Book getBookById(Long id);
}
