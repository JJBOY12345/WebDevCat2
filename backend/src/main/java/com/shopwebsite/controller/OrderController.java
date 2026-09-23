package com.shopwebsite.controller;

import com.shopwebsite.model.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final Map<String, OrderRequest> orders = new ConcurrentHashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody OrderRequest request) {
        String id = UUID.randomUUID().toString();
        orders.put(id, request);
        return Map.of("id", id, "status", "PLACED", "order", request);
    }

    @GetMapping
    public Map<String, OrderRequest> all() { return orders; }

    @GetMapping("/{id}")
    public OrderRequest one(@PathVariable String id) { return orders.get(id); }
}
