package com.example.library.config;

import com.example.library.security.CustomUserDetailsService;
import com.example.library.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomUserDetailsService customUserDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()

            // Books and Authors
            .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/authors/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/authors/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/authors/**").hasRole("ADMIN")

            // Loans
            .requestMatchers(HttpMethod.POST, "/api/loans/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/loans/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/loans/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/loans/**").hasRole("ADMIN")

            // Library Members
            .requestMatchers(HttpMethod.GET, "/api/members/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/members/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/members/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/members/**").hasRole("ADMIN")

            // Payment
            .requestMatchers(HttpMethod.POST, "/api/payments/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")

            .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .userDetailsService(customUserDetailsService)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}