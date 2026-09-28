package com.shopwebsite.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record Order(String id, String userId, List<CartItem> items, BigDecimal total,
                    String address, String paymentMethod, String status, Instant createdAt) {}
