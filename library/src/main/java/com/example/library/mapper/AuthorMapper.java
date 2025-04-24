package com.example.library.mapper;

import com.example.library.dto.AuthorDto;
import com.example.library.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

  public AuthorDto toDto(Author author) {
    AuthorDto dto = new AuthorDto();
    dto.setId(author.getId());
    dto.setName(author.getName());
    return dto;
  }

  public Author toEntity(AuthorDto dto) {
    Author author = new Author();
    author.setId(dto.getId());
    author.setName(dto.getName());
    return author;
  }
}