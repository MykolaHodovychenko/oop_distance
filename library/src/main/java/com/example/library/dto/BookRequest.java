package com.example.library.dto;

import lombok.Data;

@Data
public class BookRequest {
  private String title;
  private String isbn;
  private int publishedYear;
  private boolean available;
  private Long authorId;
}