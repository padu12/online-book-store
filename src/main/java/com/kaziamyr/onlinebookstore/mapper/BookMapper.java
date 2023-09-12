package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    default List<Long> map(Set<Category> value) {
        return value.stream()
                .map(Category::getId)
                .toList();
    }

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}
