package com.kaziamyr.onlinebookstore.dto;

import lombok.Data;

@Data
public class CreateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private String price;
    private String description;
    private String coverImage;
}
