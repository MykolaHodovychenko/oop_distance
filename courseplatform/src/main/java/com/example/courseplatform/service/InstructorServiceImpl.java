package com.example.courseplatform.service;

import com.example.courseplatform.dto.InstructorRequest;
import com.example.courseplatform.dto.InstructorResponse;
import com.example.courseplatform.mapper.InstructorMapper;
import com.example.courseplatform.model.Instructor;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.InstructorRepository;
import com.example.courseplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

  @Autowired
  private InstructorRepository instructorRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public InstructorResponse createInstructor(InstructorRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Instructor instructor = new Instructor();
    instructor.setFullName(request.getFullName());
    instructor.setUser(user);

    return InstructorMapper.toDto(instructorRepository.save(instructor));
  }

  @Override
  public List<InstructorResponse> getAllInstructors() {
    return instructorRepository.findAll().stream()
        .map(InstructorMapper::toDto)
        .collect(Collectors.toList());
  }
}