package com.example.library.controller;

import com.example.library.dto.PaymentDto;
import com.example.library.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService service;

  @PostMapping
  public ResponseEntity<PaymentDto> create(@RequestBody @Valid PaymentDto dto) {
    return ResponseEntity.ok(service.createPayment(dto));
  }

  @GetMapping
  public ResponseEntity<List<PaymentDto>> getAll() {
    return ResponseEntity.ok(service.getAllPayments());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getPaymentById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PaymentDto> update(@PathVariable Long id, @RequestBody @Valid PaymentDto dto) {
    dto.setId(id);
    return ResponseEntity.ok(service.updatePayment(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deletePayment(id);
    return ResponseEntity.noContent().build();
  }
}