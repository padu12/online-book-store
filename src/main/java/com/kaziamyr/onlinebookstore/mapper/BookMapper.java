package com.kaziamyr.onlinebookstore.mapper;

import com.kaziamyr.onlinebookstore.config.MapperConfig;
import com.kaziamyr.onlinebookstore.dto.book.BookDto;
import com.kaziamyr.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.kaziamyr.onlinebookstore.dto.book.CreateBookRequestDto;
import com.kaziamyr.onlinebookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
    //todo (HINT: BookDtoWithoutCategoryIds class could be used as a response class for
    // @GetMapping("/{id}/books") endpoint)

    //    @AfterMapping
    //        // todo should be default and check implementation in target
    //    void setCategoryIds(@MappingTarget BookDto bookDto, Book book);
    //
    //    @Named("bookFromId")
    //    Book bookFromId(Long id); // todo should be default and check implementation in target
}
