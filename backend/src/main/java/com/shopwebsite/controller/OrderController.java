package com.shopwebsite.controller;

import com.shopwebsite.model.OrderRequest;
import com.shopwebsite.model.CheckoutRequest;
import com.shopwebsite.model.Order;
import com.shopwebsite.model.UpdateOrderStatus;
import com.shopwebsite.model.User;
import com.shopwebsite.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final Map<String, OrderRequest> orders = new ConcurrentHashMap<>();
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();
    private final CartController carts;
    private final AuthService auth;
    public OrderController(CartController carts, AuthService auth) { this.carts = carts; this.auth = auth; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody OrderRequest request) {
        String id = UUID.randomUUID().toString();
        orders.put(id, request);
        return Map.of("id", id, "status", "PLACED", "order", request);
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Order checkout(@RequestHeader("Authorization") String header, @Valid @RequestBody CheckoutRequest request) {
        User user = auth.require(header.substring(7));
        var cart = carts.forUser(user.id());
        if (cart.getItems().isEmpty()) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        var items = List.copyOf(cart.getItems());
        var total = items.stream().map(i -> i.getPrice().multiply(java.math.BigDecimal.valueOf(i.getQuantity()))).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        Order order = new Order(UUID.randomUUID().toString(), user.id(), items, total, request.address(), request.paymentMethod(), "PLACED", Instant.now());
        orderStore.put(order.id(), order); cart.clear(); return order;
    }

    @GetMapping("/mine")
    public List<Order> mine(@RequestHeader("Authorization") String header) {
        String userId = auth.require(header.substring(7)).id();
        return orderStore.values().stream().filter(o -> o.userId().equals(userId)).sorted(Comparator.comparing(Order::createdAt).reversed()).toList();
    }

    @GetMapping("/manage")
    public List<Order> manage(@RequestHeader("Authorization") String header) { auth.requireAdmin(header.substring(7)); return new ArrayList<>(orderStore.values()); }

    @PatchMapping("/{id}/status")
    public Order status(@PathVariable String id, @RequestHeader("Authorization") String header, @Valid @RequestBody UpdateOrderStatus request) {
        auth.requireAdmin(header.substring(7)); Order old = orderStore.get(id);
        if (old == null) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        Order updated = new Order(old.id(), old.userId(), old.items(), old.total(), old.address(), old.paymentMethod(), request.status(), old.createdAt());
        orderStore.put(id, updated); return updated;
    }

    @GetMapping
    public Map<String, OrderRequest> all() { return orders; }

    @GetMapping("/{id}")
    public OrderRequest one(@PathVariable String id) { return orders.get(id); }
}
