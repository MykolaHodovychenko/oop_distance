package com.example.library.mapper;

import com.example.library.dto.PaymentDto;
import com.example.library.model.Loan;
import com.example.library.model.Payment;
import com.example.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

  private final LoanRepository loanRepository;

  public PaymentDto toDto(Payment payment) {
    PaymentDto dto = new PaymentDto();
    dto.setId(payment.getId());
    dto.setAmount(payment.getAmount());
    dto.setPaymentDate(payment.getPaymentDate());
    dto.setLoanId(payment.getLoan().getId());
    return dto;
  }

  public Payment toEntity(PaymentDto dto) {
    Payment payment = new Payment();
    payment.setId(dto.getId());
    payment.setAmount(dto.getAmount());
    payment.setPaymentDate(dto.getPaymentDate());

    Loan loan = loanRepository.findById(dto.getLoanId())
        .orElseThrow(() -> new RuntimeException("Loan not found"));
    payment.setLoan(loan);

    return payment;
  }
}