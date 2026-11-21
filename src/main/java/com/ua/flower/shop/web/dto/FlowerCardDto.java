package com.ua.flower.shop.web.dto;

import java.math.BigDecimal;

/**
 * Вихідний DTO: коротка картка квітки для переліку (GET /api/flowers).
 */
public record FlowerCardDto(Long id, String name, String category, String color,
                            BigDecimal price, String photoUrl) {
}
