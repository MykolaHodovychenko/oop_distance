package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.dto.BookRequest;
import com.example.library.mapper.BookMapper;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final BookMapper bookMapper;

  @Override
  public BookDto createBook(BookRequest request) {
    Author author = authorRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new RuntimeException("Автор не знайдений"));

    Book book = bookMapper.toEntity(request);
    book.setAuthor(author);

    return bookMapper.toDto(bookRepository.save(book));
  }

  @Override
  public List<BookDto> getAllBooks() {
    return bookRepository.findAll().stream()
        .map(bookMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public BookDto getBookById(Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Книга не знайдена"));
    return bookMapper.toDto(book);
  }

  @Override
  public BookDto updateBook(Long id, BookRequest request) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Книга не знайдена"));

    Author author = authorRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new RuntimeException("Автор не знайдений"));

    book.setTitle(request.getTitle());
    book.setIsbn(request.getIsbn());
    book.setPublishedYear(request.getPublishedYear());
    book.setAvailable(request.isAvailable());
    book.setAuthor(author);

    return bookMapper.toDto(bookRepository.save(book));
  }

  @Override
  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }
}