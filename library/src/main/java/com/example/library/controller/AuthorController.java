package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService service;

  @PostMapping
  public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto dto) {
    return ResponseEntity.ok(service.createAuthor(dto));
  }

  @GetMapping
  public ResponseEntity<List<AuthorDto>> getAll() {
    return ResponseEntity.ok(service.getAllAuthors());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getAuthorById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AuthorDto> update(@PathVariable Long id, @RequestBody AuthorDto dto) {
    return ResponseEntity.ok(service.updateAuthor(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteAuthor(id);
    return ResponseEntity.noContent().build();
  }
}