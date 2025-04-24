package com.example.library.config;

import com.example.library.model.*;
import com.example.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;
  private final LibraryMemberRepository memberRepository;
  private final LoanRepository loanRepository;

  @Bean
  public CommandLineRunner initAdminData() {
    return args -> {
      Role userRole = roleRepository.findByName("ROLE_USER")
          .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

      Role adminRole = roleRepository.findByName("ROLE_ADMIN")
          .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

      System.out.println("✅ Ролі ініціалізовано");

      String adminEmail = "admin@gmail.com";
      if (userRepository.findByEmail(adminEmail).isEmpty()) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(adminRole);
        userRepository.save(admin);
        System.out.println("✅ Створено користувача-адміністратора: admin");
      } else {
        System.out.println("✅ Адміністратор вже існує");
      }
    };
  }

  @Bean
  public CommandLineRunner initTestData() {
    return args -> {
      if (authorRepository.count() == 0) {
        Author author = new Author();
        author.setName("J.K. Rowling");
        author = authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setIsbn("978-0747532699");
        book.setPublishedYear(1997);
        book.setAvailable(true);
        book.setAuthor(author);
        book = bookRepository.save(book);

        LibraryMember member = new LibraryMember();
        member.setFullName("John Doe");
        member.setEmail("john@example.com");
        member = memberRepository.save(member);

        // Додаємо тестовий loan
        Loan loan = Loan.builder()
            .loanDate(LocalDate.now())
            .dueDate(LocalDate.now().plusWeeks(2))
            .returned(false)
            .book(book)
            .member(member)
            .build();
        loanRepository.save(loan);

        // Робимо книгу недоступною після видачі
        book.setAvailable(false);
        bookRepository.save(book);

        System.out.println("✅ Тестові дані (author/book/member/loan) ініціалізовані");
      }
    };
  }
}