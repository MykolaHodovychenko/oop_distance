package com.example.courseplatform.controller;

import com.example.courseplatform.dto.AuthResponse;
import com.example.courseplatform.dto.LoginRequest;
import com.example.courseplatform.dto.RegisterRequest;
import com.example.courseplatform.model.Role;
import com.example.courseplatform.model.Student;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.StudentRepository;
import com.example.courseplatform.repository.UserRepository;
import com.example.courseplatform.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      return ResponseEntity.badRequest()
          .body(new AuthResponse("Користувач з таким username вже існує"));
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    // Перетворення рядка в enum Role
    Role userRole;
    try {
      userRole = Role.valueOf(request.getRole().toUpperCase());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(new AuthResponse("Невірна роль. Можливі значення: ROLE_USER або ROLE_ADMIN"));
    }

    user.setRole(userRole);
    user = userRepository.save(user);

    if (userRole == Role.ROLE_USER) {
      Student student = new Student();
      student.setFullName(request.getFullName());
      student.setUser(user);
      studentRepository.save(student);
    }

    String token = jwtUtil.generateToken(user.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    String token = jwtUtil.generateToken(request.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
  }
}