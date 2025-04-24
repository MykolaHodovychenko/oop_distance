package com.example.courseplatform.repository;

import com.example.courseplatform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}