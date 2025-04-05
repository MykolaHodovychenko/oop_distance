package com.dariasyanska.onlinestore.security;

import com.dariasyanska.onlinestore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}