package com.example.courseplatform.service;

import com.example.courseplatform.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> findByUsername(String username);
  Optional<User> findById(Long id);
  List<User> findAll();
}