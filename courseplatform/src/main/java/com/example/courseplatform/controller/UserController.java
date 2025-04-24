package com.example.courseplatform.controller;

import com.example.courseplatform.model.User;
import com.example.courseplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  // 1. Отримати дані поточного залогіненого користувача
  @GetMapping("/me")
  public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.findByUsername(userDetails.getUsername())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 2. Отримати користувача по id (тільки ADMIN)
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 3. Отримати список усіх користувачів (тільки ADMIN)
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }
}