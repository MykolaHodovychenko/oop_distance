package com.example.library;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository,
																		UserRepository userRepository,
																		PasswordEncoder passwordEncoder) {
		return args -> {
			Role userRole = roleRepository.findByName("ROLE_USER")
					.orElseGet(() -> {
						System.out.println("✅ Створено роль: ROLE_USER");
						return roleRepository.save(new Role("ROLE_USER"));
					});

			Role adminRole = roleRepository.findByName("ROLE_ADMIN")
					.orElseGet(() -> {
						System.out.println("✅ Створено роль: ROLE_ADMIN");
						return roleRepository.save(new Role("ROLE_ADMIN"));
					});

			Optional<User> admin = userRepository.findByEmail("admin@library.com");

			if (admin.isEmpty()) {
				User adminUser = new User();
				adminUser.setUsername("admin");
				adminUser.setEmail("admin@library.com");
				adminUser.setPassword(passwordEncoder.encode("admin123")); // пароль буде захешовано
				adminUser.setRole(adminRole);
				userRepository.save(adminUser);

				System.out.println("✅ Створено користувача-адміністратора: admin");
			} else {
				System.out.println("ℹ️ Користувач-адміністратор вже існує");
			}
		};
	}
}