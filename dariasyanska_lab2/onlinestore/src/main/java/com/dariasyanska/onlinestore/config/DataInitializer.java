package com.dariasyanska.onlinestore.config;

import com.dariasyanska.onlinestore.model.Role;
import com.dariasyanska.onlinestore.model.User;
import com.dariasyanska.onlinestore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()) {
        User admin = new User("admin", passwordEncoder.encode("adminpass"), Role.ADMIN);
        userRepository.save(admin);
        System.out.println("Створено користувача: admin / adminpass");
      }

      if (userRepository.findByUsername("user").isEmpty()) {
        User user = new User("user", passwordEncoder.encode("userpass"), Role.USER);
        userRepository.save(user);
        System.out.println("Створено користувача: user / userpass");
      }
    };
  }
}