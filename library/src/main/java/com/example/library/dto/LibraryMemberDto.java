package com.example.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LibraryMemberDto {

  private Long id;

  @NotBlank(message = "Full name is required")
  private String fullName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  private String email;
}