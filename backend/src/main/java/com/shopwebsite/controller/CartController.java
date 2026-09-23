package com.shopwebsite.controller;

import com.shopwebsite.model.Cart;
import com.shopwebsite.model.CartItem;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    @GetMapping("/{cartId}")
    public Cart get(@PathVariable String cartId) { return carts.computeIfAbsent(cartId, Cart::new); }

    @PostMapping("/{cartId}/items")
    public Cart add(@PathVariable String cartId, @Valid @RequestBody CartItem incoming) {
        Cart cart = get(cartId);
        cart.getItems().stream().filter(item -> item.getProductId().equals(incoming.getProductId())).findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity() + incoming.getQuantity()),
                        () -> cart.getItems().add(incoming));
        return cart;
    }

    @PutMapping("/{cartId}/items/{productId}")
    public Cart update(@PathVariable String cartId, @PathVariable String productId, @Valid @RequestBody CartItem incoming) {
        Cart cart = get(cartId);
        cart.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst()
                .ifPresent(item -> item.setQuantity(incoming.getQuantity()));
        return cart;
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public Cart remove(@PathVariable String cartId, @PathVariable String productId) {
        Cart cart = get(cartId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cart;
    }
}
