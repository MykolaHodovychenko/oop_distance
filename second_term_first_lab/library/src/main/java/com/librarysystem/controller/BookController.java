package com.librarysystem.controller;

import com.librarysystem.model.Book;
import com.librarysystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  @PostMapping
  public Book addBook(@RequestBody Book book) {
    return bookRepository.save(book);
  }
}