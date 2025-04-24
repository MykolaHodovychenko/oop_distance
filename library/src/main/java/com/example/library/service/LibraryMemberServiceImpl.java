package com.example.library.service;

import com.example.library.dto.LibraryMemberDto;
import com.example.library.mapper.LibraryMemberMapper;
import com.example.library.model.LibraryMember;
import com.example.library.repository.LibraryMemberRepository;
import com.example.library.service.LibraryMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryMemberServiceImpl implements LibraryMemberService {

  private final LibraryMemberRepository repository;
  private final LibraryMemberMapper mapper;

  @Override
  public LibraryMemberDto createMember(LibraryMemberDto dto) {
    LibraryMember member = mapper.toEntity(dto);
    return mapper.toDto(repository.save(member));
  }

  @Override
  public List<LibraryMemberDto> getAllMembers() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public LibraryMemberDto getMemberById(Long id) {
    LibraryMember member = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    return mapper.toDto(member);
  }

  @Override
  public LibraryMemberDto updateMember(Long id, LibraryMemberDto dto) {
    LibraryMember member = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    member.setFullName(dto.getFullName());
    member.setEmail(dto.getEmail());
    return mapper.toDto(repository.save(member));
  }

  @Override
  public void deleteMember(Long id) {
    repository.deleteById(id);
  }
}