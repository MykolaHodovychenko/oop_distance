package com.example.courseplatform.controller;

import com.example.courseplatform.dto.CourseRequest;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Instructor;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private InstructorRepository instructorRepository;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Course> createCourse(@RequestBody CourseRequest request) {
    Instructor instructor = instructorRepository.findById(request.getInstructorId())
        .orElseThrow(() -> new RuntimeException("Інструктор не знайдений"));

    Course course = new Course();
    course.setTitle(request.getTitle());
    course.setDescription(request.getDescription());
    course.setDuration(request.getDuration());
    course.setInstructor(instructor);

    Course saved = courseRepository.save(course);
    return ResponseEntity.ok(saved);
  }
}