package com.example.courseplatform.dto;

public class InstructorRequest {
  private String fullName;
  private Long userId;

  // Getters and setters
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}