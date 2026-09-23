package com.shopwebsite.service;

import com.shopwebsite.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final AtomicLong ids = new AtomicLong(6);
    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();

    public ProductService() {
        save(new Product(1L, "Laptop", new BigDecimal("50000"), 10,
                "Powerful laptop for work, study and entertainment.", "💻"));
        save(new Product(2L, "Keyboard", new BigDecimal("1500"), 10,
                "Comfortable keyboard suitable for everyday use.", "⌨️"));
        save(new Product(3L, "Mouse", new BigDecimal("800"), 10,
                "Wireless mouse with smooth and accurate tracking.", "🖱️"));
        save(new Product(4L, "Headphones", new BigDecimal("2500"), 10,
                "Comfortable headphones with clear sound quality.", "🎧"));
        save(new Product(5L, "Monitor", new BigDecimal("12000"), 10,
                "Full HD monitor suitable for work and entertainment.", "🖥️"));
        save(new Product(6L, "Webcam", new BigDecimal("3000"), 10,
                "HD webcam for online classes and video meetings.", "📷"));
    }

    public List<Product> findAll() {
        return products.values().stream().sorted(Comparator.comparing(Product::getId)).toList();
    }

    public Product findById(Long id) {
        Product product = products.get(id);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        return product;
    }

    public Product create(Product product) {
        applyDefaults(product);
        product.setId(ids.incrementAndGet());
        return save(product);
    }

    public Product update(Long id, Product replacement) {
        findById(id);
        applyDefaults(replacement);
        replacement.setId(id);
        return save(replacement);
    }

    public void delete(Long id) {
        if (products.remove(id) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
    }

    private Product save(Product product) {
        if (product.getEmoji() == null || product.getEmoji().isBlank()) product.setEmoji("🛍️");
        products.put(product.getId(), product);
        return product;
    }

    private void applyDefaults(Product product) {
        if (product.getQuantity() == null) product.setQuantity(10);
    }
}
