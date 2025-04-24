package com.example.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String isbn;
  private int publishedYear;

  private boolean available;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private Author author;
}