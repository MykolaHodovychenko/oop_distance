package com.dariasyanska.onlinestore.security;

import com.dariasyanska.onlinestore.model.Role;
import com.dariasyanska.onlinestore.model.User;
import com.dariasyanska.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new RuntimeException("User already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole(Role.USER); // стандартна роль для зареєстрованих користувачів

    userRepository.save(user);
  }
}