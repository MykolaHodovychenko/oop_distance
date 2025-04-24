package com.example.library.repository;

import com.example.library.model.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
}