package com.example.library.mapper;

import com.example.library.dto.BookDto;
import com.example.library.dto.BookRequest;
import com.example.library.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  public BookDto toDto(Book book) {
    BookDto dto = new BookDto();
    dto.setId(book.getId());
    dto.setTitle(book.getTitle());
    dto.setIsbn(book.getIsbn());
    dto.setPublishedYear(book.getPublishedYear());
    dto.setAvailable(book.isAvailable());
    dto.setAuthorId(book.getAuthor().getId());
    dto.setAuthorName(book.getAuthor().getName());
    return dto;
  }

  public Book toEntity(BookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setIsbn(request.getIsbn());
    book.setPublishedYear(request.getPublishedYear());
    book.setAvailable(request.isAvailable());
    return book;
  }
}