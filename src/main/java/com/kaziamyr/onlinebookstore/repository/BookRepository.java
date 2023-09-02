package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book getBookById(Long id);

//    todo HINT: you can use the next method in the BookRepository.class:
//    List<Book> findAllByCategoryId(Long categoryId);
}
