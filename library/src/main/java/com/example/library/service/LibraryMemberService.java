package com.example.library.service;

import com.example.library.dto.LibraryMemberDto;

import java.util.List;

public interface LibraryMemberService {
  LibraryMemberDto createMember(LibraryMemberDto dto);
  List<LibraryMemberDto> getAllMembers();
  LibraryMemberDto getMemberById(Long id);
  LibraryMemberDto updateMember(Long id, LibraryMemberDto dto);
  void deleteMember(Long id);
}