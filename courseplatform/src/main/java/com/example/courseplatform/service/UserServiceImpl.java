package com.example.courseplatform.service;

import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.UserRepository;
import com.example.courseplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }
}