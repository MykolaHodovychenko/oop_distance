package com.example.courseplatform.controller;

import com.example.courseplatform.dto.CourseResponse;
import com.example.courseplatform.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

  @Autowired
  private StudentService studentService;

  @PostMapping("/{studentId}/courses/{courseId}")
  public ResponseEntity<Void> enrollToCourse(
      @PathVariable Long studentId,
      @PathVariable Long courseId
  ) {
    studentService.enrollStudentToCourse(studentId, courseId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{studentId}/courses")
  public ResponseEntity<List<CourseResponse>> getEnrolledCourses(@PathVariable Long studentId) {
    return ResponseEntity.ok(studentService.getCoursesOfStudent(studentId));
  }
}