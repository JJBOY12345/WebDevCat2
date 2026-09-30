package com.shopwebsite.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repository;

    private final JwtService jwt;

    public ProductController(ProductRepository repository, @Value("${jwt.secret}") String secret) {
        this.repository = repository;
        this.jwt = new JwtService(secret);
    }

    @GetMapping
    public List<Product> all(@RequestParam(required = false) String q) {
        if (q == null || q.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q.trim(), q.trim());
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestHeader("Authorization") String authorization, @Valid @RequestBody Product product) {
        admin(authorization);
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@RequestHeader("Authorization") String authorization, @PathVariable String id,
                          @Valid @RequestBody Product product) {
        admin(authorization);
        one(id);
        product.setId(id);
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("Authorization") String authorization, @PathVariable String id) {
        admin(authorization);
        repository.delete(one(id));
    }

    @PatchMapping("/{id}/reserve")
    public Product reserve(@PathVariable String id, @RequestParam int quantity) {
        if (quantity < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        Product product = one(id);
        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
        return repository.save(product);
    }

    @PostMapping("/{id}/reserve")
    public Product reserveForService(@PathVariable String id, @RequestParam int quantity) {
        return reserve(id, quantity);
    }

    private void admin(String authorization) {
        try {
            if (!"ADMIN".equals(jwt.parse(authorization).get("role", String.class))) throw new Exception();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }
}
