package com.example.courseplatform.mapper;

import com.example.courseplatform.dto.CourseResponse;
import com.example.courseplatform.model.Course;

public class CourseMapper {
  public static CourseResponse toDto(Course course) {
    CourseResponse dto = new CourseResponse();
    dto.setId(course.getId());
    dto.setTitle(course.getTitle());
    dto.setDescription(course.getDescription());
    dto.setInstructorName(course.getInstructor().getFullName());
    return dto;
  }
}