package com.example.courseplatform.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "Full name is required")
  private String fullName;

  @NotBlank(message = "Role is required")
  private String role;

  // Конструктори
  public RegisterRequest() {}

  public RegisterRequest(String username, String password, String fullName, String role) {
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.role = role;
  }

  // Геттери і сеттери
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}