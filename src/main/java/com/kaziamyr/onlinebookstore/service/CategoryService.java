package com.kaziamyr.onlinebookstore.service;

import com.kaziamyr.onlinebookstore.dto.category.CategoryDto;
import com.kaziamyr.onlinebookstore.dto.category.SaveCategoryRequestDto;
import com.kaziamyr.onlinebookstore.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(SaveCategoryRequestDto request);

    CategoryDto update(Long id, SaveCategoryRequestDto request);

    void deleteById(Long id);
}
