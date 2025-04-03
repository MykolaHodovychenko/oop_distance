package com.librarysystem.controller;

import com.librarysystem.model.Book;
import com.librarysystem.model.LibraryMember;
import com.librarysystem.repository.BookRepository;
import com.librarysystem.repository.LibraryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class LibraryMemberController {
  @Autowired
  private LibraryMemberRepository memberRepository;

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public List<LibraryMember> getAllMembers() {
    return memberRepository.findAll();
  }

  @PostMapping
  public LibraryMember addMember(@RequestBody LibraryMember member) {
    return memberRepository.save(member);
  }

  @PostMapping("/{memberId}/borrow/{bookId}")
  public LibraryMember borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
    LibraryMember member = memberRepository.findById(memberId)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    member.getBorrowedBooks().add(book);
    return memberRepository.save(member);
  }
}