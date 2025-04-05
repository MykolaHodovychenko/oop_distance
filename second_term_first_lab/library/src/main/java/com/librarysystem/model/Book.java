package com.librarysystem.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private Author author;

  @ManyToMany(mappedBy = "borrowedBooks")
  private Set<LibraryMember> members = new HashSet<>();

  public Book() {}

  public Book(String title, Author author) {
    this.title = title;
    this.author = author;
  }

  // Геттери і сеттери
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public Author getAuthor() { return author; }
  public void setAuthor(Author author) { this.author = author; }
  public Set<LibraryMember> getMembers() { return members; }
  public void setMembers(Set<LibraryMember> members) { this.members = members; }
}