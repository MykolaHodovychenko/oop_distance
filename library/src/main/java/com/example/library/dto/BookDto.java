package com.example.library.dto;

import lombok.Data;

@Data
public class BookDto {
  private Long id;
  private String title;
  private String isbn;
  private int publishedYear;
  private boolean available;

  private Long authorId;
  private String authorName;
}