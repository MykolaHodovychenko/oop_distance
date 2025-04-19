package com.example.courseplatform.mapper;

import com.example.courseplatform.dto.LessonRequest;
import com.example.courseplatform.dto.LessonResponse;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Lesson;

public class LessonMapper {

  public static LessonResponse toDto(Lesson lesson) {
    LessonResponse dto = new LessonResponse();
    dto.setId(lesson.getId());
    dto.setTitle(lesson.getTitle());
    dto.setContent(lesson.getContent());
    dto.setCourseTitle(lesson.getCourse().getTitle());
    return dto;
  }

  public static Lesson toEntity(LessonRequest request, Course course) {
    Lesson lesson = new Lesson();
    lesson.setTitle(request.getTitle());
    lesson.setContent(request.getContent());
    lesson.setCourse(course);
    return lesson;
  }
}