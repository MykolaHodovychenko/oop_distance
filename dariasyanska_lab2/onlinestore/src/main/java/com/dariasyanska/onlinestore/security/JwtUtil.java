package com.dariasyanska.onlinestore.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

  // ❗ Тут має бути base64-кодований ключ
  private final String SECRET_KEY = "c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0"; // "secretsecretsecretsecretsecret" в base64

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(UserDetails userDetails) {
    long expirationTimeMs = 1000L * 60 * 60 * 24 * 30; // 30 днів у мілісекундах
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    return extractUsername(token).equals(userDetails.getUsername()) &&
        !extractClaim(token, Claims::getExpiration).before(new Date());
  }
}