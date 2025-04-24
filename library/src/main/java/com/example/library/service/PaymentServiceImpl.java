package com.example.library.service;

import com.example.library.dto.PaymentDto;
import com.example.library.exception.EntityNotFoundException;
import com.example.library.mapper.PaymentMapper;
import com.example.library.model.Payment;
import com.example.library.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository repository;
  private final PaymentMapper mapper;

  @Override
  public PaymentDto createPayment(PaymentDto dto) {
    Payment payment = mapper.toEntity(dto);
    return mapper.toDto(repository.save(payment));
  }

  @Override
  public List<PaymentDto> getAllPayments() {
    return repository.findAll().stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public PaymentDto getPaymentById(Long id) {
    return repository.findById(id)
        .map(mapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Payment with id " + id + " not found"));
  }

  @Override
  public PaymentDto updatePayment(PaymentDto dto) {
    Payment existing = repository.findById(dto.getId())
        .orElseThrow(() -> new EntityNotFoundException("Payment with id " + dto.getId() + " not found"));
    Payment updated = mapper.toEntity(dto);
    updated.setId(existing.getId());
    return mapper.toDto(repository.save(updated));
  }

  @Override
  public void deletePayment(Long id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Payment with id " + id + " not found");
    }
    repository.deleteById(id);
  }
}