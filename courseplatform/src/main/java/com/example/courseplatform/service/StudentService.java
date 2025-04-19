package com.example.courseplatform.service;

import com.example.courseplatform.dto.CourseResponse;

import java.util.List;

public interface StudentService {
  void enrollStudentToCourse(Long studentId, Long courseId);
  List<CourseResponse> getCoursesOfStudent(Long studentId);
}
