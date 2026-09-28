package com.shopwebsite.controller;

import com.shopwebsite.model.Product;
import com.shopwebsite.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/products", "/products"})
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping public List<Product> all() { return service.findAll(); }
    @GetMapping("/{id}") public Product one(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Product create(@Valid @RequestBody Product product) { return service.create(product); }
    @PutMapping("/{id}") public Product update(@PathVariable Long id, @Valid @RequestBody Product product) { return service.update(id, product); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }

    @PostMapping("/admin") @ResponseStatus(HttpStatus.CREATED)
    public Product adminCreate(@RequestHeader("Authorization") String header, @Valid @RequestBody Product product) {
        service.requireAdmin(header); return service.create(product);
    }
    @PutMapping("/admin/{id}")
    public Product adminUpdate(@RequestHeader("Authorization") String header, @PathVariable Long id, @Valid @RequestBody Product product) {
        service.requireAdmin(header); return service.update(id, product);
    }
    @DeleteMapping("/admin/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDelete(@RequestHeader("Authorization") String header, @PathVariable Long id) { service.requireAdmin(header); service.delete(id); }
}
