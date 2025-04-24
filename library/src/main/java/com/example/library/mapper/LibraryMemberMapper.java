package com.example.library.mapper;

import com.example.library.dto.LibraryMemberDto;
import com.example.library.model.LibraryMember;
import org.springframework.stereotype.Component;

@Component
public class LibraryMemberMapper {

  public LibraryMemberDto toDto(LibraryMember member) {
    LibraryMemberDto dto = new LibraryMemberDto();
    dto.setId(member.getId());
    dto.setFullName(member.getFullName());
    dto.setEmail(member.getEmail());
    return dto;
  }

  public LibraryMember toEntity(LibraryMemberDto dto) {
    LibraryMember member = new LibraryMember();
    member.setId(dto.getId());
    member.setFullName(dto.getFullName());
    member.setEmail(dto.getEmail());
    return member;
  }
}