package com.example.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "library_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fullName;

  private String email;
}