package com.example.courseplatform.service;

import com.example.courseplatform.dto.CourseRequest;
import com.example.courseplatform.dto.CourseResponse;

import java.util.List;

public interface CourseService {
  CourseResponse createCourse(CourseRequest request);
  List<CourseResponse> getAllCourses();
}