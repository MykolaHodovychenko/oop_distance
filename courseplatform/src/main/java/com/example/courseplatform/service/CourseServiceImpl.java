package com.example.courseplatform.service;

import com.example.courseplatform.dto.CourseRequest;
import com.example.courseplatform.dto.CourseResponse;
import com.example.courseplatform.mapper.CourseMapper;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Instructor;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.InstructorRepository;
import com.example.courseplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private InstructorRepository instructorRepository;

  @Override
  public CourseResponse createCourse(CourseRequest request) {
    Instructor instructor = instructorRepository.findById(request.getInstructorId())
        .orElseThrow(() -> new RuntimeException("Instructor not found"));

    Course course = new Course();
    course.setTitle(request.getTitle());
    course.setDescription(request.getDescription());
    course.setInstructor(instructor);

    return CourseMapper.toDto(courseRepository.save(course));
  }

  @Override
  public List<CourseResponse> getAllCourses() {
    return courseRepository.findAll()
        .stream()
        .map(CourseMapper::toDto)
        .collect(Collectors.toList());
  }
}