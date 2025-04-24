package com.example.library.mapper;

import com.example.library.dto.LoanDto;
import com.example.library.exception.EntityNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.LibraryMember;
import com.example.library.model.Loan;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LibraryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanMapper {

  private final BookRepository bookRepository;
  private final LibraryMemberRepository memberRepository;

  public LoanDto toDto(Loan loan) {
    LoanDto dto = new LoanDto();
    dto.setId(loan.getId());
    dto.setLoanDate(loan.getLoanDate());
    dto.setDueDate(loan.getDueDate());
    dto.setReturned(loan.isReturned());
    dto.setBookId(loan.getBook().getId());
    dto.setBookTitle(loan.getBook().getTitle());
    dto.setMemberId(loan.getMember().getId());
    dto.setMemberName(loan.getMember().getFullName());
    return dto;
  }

  public Loan toEntity(LoanDto dto) {
    Loan loan = new Loan();
    loan.setId(dto.getId());
    loan.setLoanDate(dto.getLoanDate());
    loan.setDueDate(dto.getDueDate());
    loan.setReturned(dto.isReturned());

    Book book = bookRepository.findById(dto.getBookId())
        .orElseThrow(() -> new EntityNotFoundException("Book with id " + dto.getBookId() + " not found"));
    LibraryMember member = memberRepository.findById(dto.getMemberId())
        .orElseThrow(() -> new EntityNotFoundException("Member with id " + dto.getMemberId() + " not found"));

    loan.setBook(book);
    loan.setMember(member);

    return loan;
  }
}