package com.shopwebsite.controller;

import com.shopwebsite.model.AuthRequest;
import com.shopwebsite.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }
    @PostMapping("/register") public Map<String, Object> register(@Valid @RequestBody AuthRequest request) { return auth.register(request); }
    @PostMapping("/login") public Map<String, Object> login(@Valid @RequestBody AuthRequest request) { return auth.login(request); }
    @GetMapping("/me") public Map<String, Object> me(@RequestHeader("Authorization") String header) { return auth.publicUser(auth.require(token(header))); }
    private String token(String header) { return header.replaceFirst("Bearer ", ""); }
}
