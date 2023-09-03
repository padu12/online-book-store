package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.category.CategoryDto;
import com.kaziamyr.onlinebookstore.dto.category.SaveCategoryRequestDto;
import com.kaziamyr.onlinebookstore.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid SaveCategoryRequestDto request) {
        return categoryService.save(request);
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid SaveCategoryRequestDto request) {
        return categoryService.update(id, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return categoryService.findAllBooksByCategoryId(id);
    }
}
