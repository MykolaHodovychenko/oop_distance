package com.example.courseplatform.service;

import com.example.courseplatform.dto.CourseResponse;
import com.example.courseplatform.mapper.CourseMapper;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Student;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.StudentRepository;
import com.example.courseplatform.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Override
  public void enrollStudentToCourse(Long studentId, Long courseId) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new RuntimeException("Student not found"));
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Course not found"));

    student.getCourses().add(course);
    studentRepository.save(student);
  }

  @Override
  public List<CourseResponse> getCoursesOfStudent(Long studentId) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new RuntimeException("Student not found"));

    return student.getCourses().stream()
        .map(CourseMapper::toDto)
        .collect(Collectors.toList());
  }
}
