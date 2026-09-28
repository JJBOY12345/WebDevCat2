package com.shopwebsite.model;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(@NotBlank String address, @NotBlank String paymentMethod) {}
