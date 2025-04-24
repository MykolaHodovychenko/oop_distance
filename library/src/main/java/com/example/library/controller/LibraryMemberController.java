package com.example.library.controller;

import com.example.library.dto.LibraryMemberDto;
import com.example.library.service.LibraryMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class LibraryMemberController {

  private final LibraryMemberService service;

  @PostMapping
  public ResponseEntity<LibraryMemberDto> create(@RequestBody @Valid LibraryMemberDto dto) {
    return ResponseEntity.ok(service.createMember(dto));
  }

  @GetMapping
  public ResponseEntity<List<LibraryMemberDto>> getAll() {
    return ResponseEntity.ok(service.getAllMembers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<LibraryMemberDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getMemberById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<LibraryMemberDto> update(@PathVariable Long id, @RequestBody @Valid LibraryMemberDto dto) {
    return ResponseEntity.ok(service.updateMember(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteMember(id);
    return ResponseEntity.noContent().build();
  }
}