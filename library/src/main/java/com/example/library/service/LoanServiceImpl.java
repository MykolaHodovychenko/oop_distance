package com.example.library.service;

import com.example.library.dto.LoanDto;
import com.example.library.exception.BookAlreadyReturnedException;
import com.example.library.exception.BookUnavailableException;
import com.example.library.exception.EntityNotFoundException;
import com.example.library.mapper.LoanMapper;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

  private final LoanRepository repository;
  private final BookRepository bookRepository;
  private final LoanMapper mapper;

  @Override
  public LoanDto createLoan(LoanDto dto) {
    Book book = bookRepository.findById(dto.getBookId())
        .orElseThrow(() -> new EntityNotFoundException("Book with id " + dto.getBookId() + " not found"));

    if (!book.isAvailable()) {
      throw new BookUnavailableException("Book is currently unavailable.");
    }

    Loan loan = mapper.toEntity(dto);
    loan.setBook(book);

    book.setAvailable(false);
    bookRepository.save(book);

    return mapper.toDto(repository.save(loan));
  }

  @Override
  public List<LoanDto> getAllLoans() {
    return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
  }

  @Override
  public List<LoanDto> getLoansByMember(Long memberId) {
    return repository.findByMemberId(memberId).stream().map(mapper::toDto).collect(Collectors.toList());
  }

  @Override
  public LoanDto returnBook(Long loanId) {
    Loan loan = repository.findById(loanId)
        .orElseThrow(() -> new EntityNotFoundException("Loan with id " + loanId + " not found"));

    if (loan.isReturned()) {
      throw new BookAlreadyReturnedException("Book already returned.");
    }

    loan.setReturned(true);
    loan.getBook().setAvailable(true);
    bookRepository.save(loan.getBook());
    return mapper.toDto(repository.save(loan));
  }

  @Override
  public void deleteLoan(Long id) {
    repository.deleteById(id);
  }
}