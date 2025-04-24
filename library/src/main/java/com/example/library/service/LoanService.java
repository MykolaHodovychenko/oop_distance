package com.example.library.service;

import com.example.library.dto.LoanDto;

import java.util.List;

public interface LoanService {
  LoanDto createLoan(LoanDto dto);
  List<LoanDto> getAllLoans();
  List<LoanDto> getLoansByMember(Long memberId);
  LoanDto returnBook(Long loanId);
  void deleteLoan(Long id);
}