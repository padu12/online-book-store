package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book getBookById(Long id);

    List<BookDtoWithoutCategoryIds> findAllByCategoriesIn(Set<Category> categories);
}
