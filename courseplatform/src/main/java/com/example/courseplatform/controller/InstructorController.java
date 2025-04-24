package com.example.courseplatform.controller;

import com.example.courseplatform.dto.InstructorRequest;
import com.example.courseplatform.dto.InstructorResponse;
import com.example.courseplatform.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

  @Autowired
  private InstructorService instructorService;

  // 🔐 Тільки адміну
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<InstructorResponse> createInstructor(@RequestBody InstructorRequest request) {
    InstructorResponse savedInstructor = instructorService.createInstructor(request);
    return ResponseEntity.ok(savedInstructor);
  }

  // 🔓 Можна всім аутентифікованим
  @GetMapping
  public ResponseEntity<List<InstructorResponse>> getAllInstructors() {
    return ResponseEntity.ok(instructorService.getAllInstructors());
  }
}