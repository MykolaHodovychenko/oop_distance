package com.example.library.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDto {

  private Long id;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
  private BigDecimal amount;

  @NotNull(message = "Payment date is required")
  private LocalDate paymentDate;

  @NotNull(message = "Loan ID is required")
  private Long loanId;
}