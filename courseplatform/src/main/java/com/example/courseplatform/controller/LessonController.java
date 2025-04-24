package com.example.courseplatform.controller;

import com.example.courseplatform.dto.LessonRequest;
import com.example.courseplatform.dto.LessonResponse;
import com.example.courseplatform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

  @Autowired
  private LessonService lessonService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<LessonResponse> createLesson(@RequestBody LessonRequest request, Principal principal) {
    return ResponseEntity.ok(lessonService.createLesson(request, principal.getName()));
  }

  @GetMapping("/course/{courseId}")
  public ResponseEntity<List<LessonResponse>> getLessonsByCourse(@PathVariable Long courseId) {
    return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
  }
}