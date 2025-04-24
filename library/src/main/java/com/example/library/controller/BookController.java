package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.dto.BookRequest;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService service;

  @PostMapping
  public ResponseEntity<BookDto> create(@RequestBody BookRequest request) {
    return ResponseEntity.ok(service.createBook(request));
  }

  @GetMapping
  public ResponseEntity<List<BookDto>> getAll() {
    return ResponseEntity.ok(service.getAllBooks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getBookById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody BookRequest request) {
    return ResponseEntity.ok(service.updateBook(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteBook(id);
    return ResponseEntity.noContent().build();
  }
}