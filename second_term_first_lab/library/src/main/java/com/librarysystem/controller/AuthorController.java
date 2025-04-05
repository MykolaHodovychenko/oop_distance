package com.librarysystem.controller;

import com.librarysystem.model.Author;
import com.librarysystem.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
  @Autowired
  private AuthorRepository authorRepository;

  @GetMapping
  public List<Author> getAllAuthors() {
    return authorRepository.findAll();
  }

  @PostMapping
  public Author addAuthor(@RequestBody Author author) {
    return authorRepository.save(author);
  }
}