package com.shopwebsite.service;

import com.shopwebsite.model.AuthRequest;
import com.shopwebsite.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    public AuthService() {
        users.put("admin@shop.local", new User("admin", "Store Admin", "admin@shop.local", "admin123", "ADMIN"));
        users.put("demo@shop.local", new User("demo", "Demo Customer", "demo@shop.local", "demo123", "CUSTOMER"));
    }

    public Map<String, Object> register(AuthRequest request) {
        String email = request.email().toLowerCase();
        if (users.containsKey(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        String name = request.name() == null || request.name().isBlank() ? email.substring(0, email.indexOf('@')) : request.name();
        User user = new User(UUID.randomUUID().toString(), name, email, request.password(), "CUSTOMER");
        users.put(email, user);
        return login(request);
    }

    public Map<String, Object> login(AuthRequest request) {
        User user = users.get(request.email().toLowerCase());
        if (user == null || !user.password().equals(request.password()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        return Map.of("token", token, "user", publicUser(user));
    }

    public User require(String token) {
        User user = sessions.get(token);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        return user;
    }

    public void requireAdmin(String token) {
        if (!"ADMIN".equals(require(token).role())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
    }

    public Map<String, Object> publicUser(User user) {
        return Map.of("id", user.id(), "name", user.name(), "email", user.email(), "role", user.role());
    }
}
