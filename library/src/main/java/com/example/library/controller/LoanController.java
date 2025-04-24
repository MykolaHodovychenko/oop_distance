package com.example.library.controller;

import com.example.library.dto.LoanDto;
import com.example.library.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

  private final LoanService service;

  @PostMapping
  public ResponseEntity<LoanDto> create(@RequestBody @Valid LoanDto dto) {
    return ResponseEntity.ok(service.createLoan(dto));
  }

  @GetMapping
  public ResponseEntity<List<LoanDto>> getAll() {
    return ResponseEntity.ok(service.getAllLoans());
  }

  @GetMapping("/member/{memberId}")
  public ResponseEntity<List<LoanDto>> getByMember(@PathVariable Long memberId) {
    return ResponseEntity.ok(service.getLoansByMember(memberId));
  }

  @PutMapping("/return/{loanId}")
  public ResponseEntity<LoanDto> returnBook(@PathVariable Long loanId) {
    return ResponseEntity.ok(service.returnBook(loanId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteLoan(id);
    return ResponseEntity.noContent().build();
  }
}