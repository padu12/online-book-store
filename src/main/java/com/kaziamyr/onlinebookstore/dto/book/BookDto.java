package com.kaziamyr.onlinebookstore.dto.book;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private List<Long> categories;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
