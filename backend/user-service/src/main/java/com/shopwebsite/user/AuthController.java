package com.shopwebsite.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository repository,
            @Value("${jwt.secret}") String secret) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtService = new JwtService(secret);
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@Valid @RequestBody Credentials credentials) {
        if (repository.findByEmailIgnoreCase(credentials.email()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        User user = new User(
                credentials.name(),
                credentials.email().toLowerCase(),
                passwordEncoder.encode(credentials.password()),
                "CUSTOMER");

        return createSession(repository.save(user));
    }

    @PostMapping("/auth/login")
    public Map<String, Object> login(@Valid @RequestBody Credentials credentials) {
        User user = repository.findByEmailIgnoreCase(credentials.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid credentials"));

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials");
        }

        return createSession(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"));
    }

    @GetMapping("/auth/me")
    public Map<String, Object> currentUser(
            @RequestHeader("Authorization") String authorizationHeader) {
        return publicUser(requireUser(authorizationHeader));
    }

    private Map<String, Object> createSession(User user) {
        return Map.of(
                "token", jwtService.issue(user.getId(), user.getEmail(), user.getRole()),
                "user", publicUser(user));
    }

    private User requireUser(String authorizationHeader) {
        try {
            String userId = jwtService.parse(authorizationHeader).getSubject();

            return repository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token");
        }
    }

    private Map<String, Object> publicUser(User user) {
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole());
    }

    public record Credentials(
            String name,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6) String password) {
    }
}
