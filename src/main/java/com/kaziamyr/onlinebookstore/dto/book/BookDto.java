package com.kaziamyr.onlinebookstore.dto.book;

import com.kaziamyr.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Set<Category> categories;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
