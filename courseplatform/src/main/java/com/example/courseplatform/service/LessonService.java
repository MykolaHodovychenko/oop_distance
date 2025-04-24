package com.example.courseplatform.service;

import com.example.courseplatform.dto.LessonRequest;
import com.example.courseplatform.dto.LessonResponse;

import java.util.List;

public interface LessonService {
  LessonResponse createLesson(LessonRequest request, String username);
  List<LessonResponse> getLessonsByCourseId(Long courseId);
}