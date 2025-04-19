package com.example.courseplatform.service;

import com.example.courseplatform.dto.InstructorRequest;
import com.example.courseplatform.dto.InstructorResponse;

import java.util.List;

public interface InstructorService {
  InstructorResponse createInstructor(InstructorRequest request);
  List<InstructorResponse> getAllInstructors();
}