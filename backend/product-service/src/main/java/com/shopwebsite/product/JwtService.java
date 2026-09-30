package com.shopwebsite.product;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtService {
    private final SecretKey key;
    public JwtService(String secret) {
        if (secret == null || secret.length() < 32) throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String issue(String id, String email, String role) {
        Date now = new Date();
        return Jwts.builder().subject(id).claim("email", email).claim("role", role)
                .issuedAt(now).expiration(new Date(now.getTime() + 8 * 60 * 60 * 1000))
                .signWith(key).compact();
    }
    public Claims parse(String header) {
        if (header == null || !header.startsWith("Bearer ")) throw new IllegalArgumentException("Bearer token required");
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(header.substring(7)).getPayload();
    }
}
