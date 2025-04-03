package com.librarysystem.repository;

import com.librarysystem.model.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
}