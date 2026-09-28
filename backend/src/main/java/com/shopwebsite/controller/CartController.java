package com.shopwebsite.controller;

import com.shopwebsite.model.Cart;
import com.shopwebsite.model.CartItem;
import com.shopwebsite.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();
    private final AuthService auth;
    public CartController(AuthService auth) { this.auth = auth; }

    @GetMapping("/{cartId}")
    public Cart get(@PathVariable String cartId, @RequestHeader(value = "Authorization", required = false) String header) {
        String key = owner(header, cartId);
        return carts.computeIfAbsent(key, Cart::new);
    }

    @PostMapping("/{cartId}/items")
    public Cart add(@PathVariable String cartId, @Valid @RequestBody CartItem incoming, @RequestHeader(value = "Authorization", required = false) String header) {
        Cart cart = get(cartId, header);
        cart.getItems().stream().filter(item -> item.getProductId().equals(incoming.getProductId())).findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity() + incoming.getQuantity()),
                        () -> cart.getItems().add(incoming));
        return cart;
    }

    @PutMapping("/{cartId}/items/{productId}")
    public Cart update(@PathVariable String cartId, @PathVariable String productId, @Valid @RequestBody CartItem incoming, @RequestHeader(value = "Authorization", required = false) String header) {
        Cart cart = get(cartId, header);
        cart.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst()
                .ifPresent(item -> item.setQuantity(incoming.getQuantity()));
        return cart;
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public Cart remove(@PathVariable String cartId, @PathVariable String productId, @RequestHeader(value = "Authorization", required = false) String header) {
        Cart cart = get(cartId, header);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cart;
    }

    public Cart forUser(String userId) { return carts.computeIfAbsent(userId, Cart::new); }
    private String owner(String header, String fallback) {
        if (header == null || !header.startsWith("Bearer ")) return fallback;
        return auth.require(header.substring(7)).id();
    }
}
