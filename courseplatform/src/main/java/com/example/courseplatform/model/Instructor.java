package com.example.courseplatform.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Instructor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fullName;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "instructor")
  private List<Course> courses;

  // Конструктори
  public Instructor() {}

  public Instructor(String fullName, User user) {
    this.fullName = fullName;
    this.user = user;
  }

  // Геттери і сеттери
  public Long getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }
}