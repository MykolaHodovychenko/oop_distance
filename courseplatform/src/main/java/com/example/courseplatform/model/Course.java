package com.example.courseplatform.model;

import jakarta.persistence.*;

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private int duration;

  @ManyToOne
  @JoinColumn(name = "instructor_id", nullable = false)
  private Instructor instructor;

  // Конструктори
  public Course() {}

  public Course(String title, String description, int duration, Instructor instructor) {
    this.title = title;
    this.description = description;
    this.duration = duration;
    this.instructor = instructor;
  }

  // Геттери і сеттери
  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public Instructor getInstructor() {
    return instructor;
  }

  public void setInstructor(Instructor instructor) {
    this.instructor = instructor;
  }
}