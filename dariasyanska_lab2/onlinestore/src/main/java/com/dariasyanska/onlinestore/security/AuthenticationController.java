package com.dariasyanska.onlinestore.security;

import com.dariasyanska.onlinestore.dto.LoginRequest;
import com.dariasyanska.onlinestore.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtUtil.generateToken(userDetails);
    return ResponseEntity.ok(new LoginResponse(token));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody LoginRequest request) {
    userService.register(request.getUsername(), request.getPassword());
    return ResponseEntity.ok("User registered successfully");
  }
}