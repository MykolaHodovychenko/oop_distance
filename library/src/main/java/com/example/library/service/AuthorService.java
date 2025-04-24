package com.example.library.service;

import com.example.library.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
  AuthorDto createAuthor(AuthorDto dto);
  List<AuthorDto> getAllAuthors();
  AuthorDto getAuthorById(Long id);
  AuthorDto updateAuthor(Long id, AuthorDto dto);
  void deleteAuthor(Long id);
}