package com.librarysystem.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class LibraryMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "member_books",
      joinColumns = @JoinColumn(name = "member_id"),
      inverseJoinColumns = @JoinColumn(name = "book_id")
  )
  private Set<Book> borrowedBooks = new HashSet<>();

  public LibraryMember() {}

  public LibraryMember(String name) {
    this.name = name;
  }

  // Геттери і сеттери
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Set<Book> getBorrowedBooks() { return borrowedBooks; }
  public void setBorrowedBooks(Set<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }
}