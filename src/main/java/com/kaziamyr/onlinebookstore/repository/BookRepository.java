package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book getBookById(Long id);
}
