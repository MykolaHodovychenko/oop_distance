package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.dto.BookRequest;
import java.util.List;

public interface BookService {
  BookDto createBook(BookRequest request);
  List<BookDto> getAllBooks();
  BookDto getBookById(Long id);
  BookDto updateBook(Long id, BookRequest request);
  void deleteBook(Long id);
}