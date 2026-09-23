package com.shopwebsite.model;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(String cartId, List<CartItem> items, BigDecimal total) {}
