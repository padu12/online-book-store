package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
