package com.example.library.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {

  private Long id;

  @NotNull(message = "Book ID is required")
  private Long bookId;

  @NotNull(message = "Library member ID is required")
  private Long memberId;

  @NotNull(message = "Loan date is required")
  private LocalDate loanDate;

  @NotNull(message = "Due date is required")
  @FutureOrPresent(message = "Due date cannot be in the past")
  private LocalDate dueDate;

  private boolean returned;

  private String bookTitle;
  private String memberName;
}