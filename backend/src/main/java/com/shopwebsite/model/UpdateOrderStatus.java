package com.shopwebsite.model;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatus(@NotBlank String status) {}
