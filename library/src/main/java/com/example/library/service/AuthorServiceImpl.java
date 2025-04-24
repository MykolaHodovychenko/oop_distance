package com.example.library.service;

import com.example.library.dto.AuthorDto;
import com.example.library.mapper.AuthorMapper;
import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import com.example.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository repository;
  private final AuthorMapper mapper;

  @Override
  public AuthorDto createAuthor(AuthorDto dto) {
    Author author = mapper.toEntity(dto);
    return mapper.toDto(repository.save(author));
  }

  @Override
  public List<AuthorDto> getAllAuthors() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public AuthorDto getAuthorById(Long id) {
    Author author = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Author not found"));
    return mapper.toDto(author);
  }

  @Override
  public AuthorDto updateAuthor(Long id, AuthorDto dto) {
    Author author = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Author not found"));
    author.setName(dto.getName());
    return mapper.toDto(repository.save(author));
  }

  @Override
  public void deleteAuthor(Long id) {
    repository.deleteById(id);
  }
}