package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book getBookById(Long id);

    @Query("FROM Book b INNER JOIN FETCH b.categories c WHERE c.id=:categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
