package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.category.CategoryDto;
import com.kaziamyr.onlinebookstore.dto.category.SaveCategoryRequestDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    List<BookDtoWithoutCategoryIds> findAllBooksByCategoryId(Long id);

    CategoryDto getById(Long id);

    CategoryDto save(SaveCategoryRequestDto request);

    CategoryDto update(Long id, SaveCategoryRequestDto request);

    void deleteById(Long id);
}
