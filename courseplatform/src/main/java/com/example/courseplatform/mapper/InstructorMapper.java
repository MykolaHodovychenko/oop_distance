package com.example.courseplatform.mapper;

import com.example.courseplatform.dto.InstructorResponse;
import com.example.courseplatform.model.Instructor;

public class InstructorMapper {

  public static InstructorResponse toDto(Instructor instructor) {
    InstructorResponse dto = new InstructorResponse();
    dto.setId(instructor.getId());
    dto.setFullName(instructor.getFullName());
    dto.setUsername(instructor.getUser().getUsername());
    return dto;
  }
}