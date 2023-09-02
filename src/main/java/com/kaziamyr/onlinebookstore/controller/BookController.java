package com.kaziamyr.onlinebookstore.controller;

import com.kaziamyr.onlinebookstore.dto.BookDto;
import com.kaziamyr.onlinebookstore.dto.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateById(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.updateById(id, bookDto);
    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam Map<String, String> params) {
        return bookService.getAll(params);
    }

}
