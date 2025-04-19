package com.example.courseplatform.service;

import com.example.courseplatform.dto.LessonRequest;
import com.example.courseplatform.dto.LessonResponse;
import com.example.courseplatform.mapper.LessonMapper;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Instructor;
import com.example.courseplatform.model.Lesson;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.InstructorRepository;
import com.example.courseplatform.repository.LessonRepository;
import com.example.courseplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private InstructorRepository instructorRepository;

  @Override
  public LessonResponse createLesson(LessonRequest request, String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Instructor instructor = instructorRepository.findAll()
        .stream()
        .filter(i -> i.getUser().getId().equals(user.getId()))
        .findFirst()
        .orElseThrow(() -> new AccessDeniedException("Only instructors can create lessons"));

    Course course = courseRepository.findById(request.getCourseId())
        .orElseThrow(() -> new RuntimeException("Course not found"));

    Lesson lesson = LessonMapper.toEntity(request, course);
    return LessonMapper.toDto(lessonRepository.save(lesson));
  }

  @Override
  public List<LessonResponse> getLessonsByCourseId(Long courseId) {
    return lessonRepository.findAll()
        .stream()
        .filter(lesson -> lesson.getCourse().getId().equals(courseId))
        .map(LessonMapper::toDto)
        .collect(Collectors.toList());
  }
}