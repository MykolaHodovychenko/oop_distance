package com.example.library.service;

import com.example.library.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
  PaymentDto createPayment(PaymentDto dto);
  List<PaymentDto> getAllPayments();
  PaymentDto getPaymentById(Long id);
  PaymentDto updatePayment(PaymentDto dto);
  void deletePayment(Long id);
}