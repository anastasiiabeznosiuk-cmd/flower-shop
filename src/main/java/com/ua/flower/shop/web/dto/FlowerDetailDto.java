package com.ua.flower.shop.web.dto;

import java.math.BigDecimal;

/**
 * Вихідний DTO: повна інформація про квітку (GET /api/flowers/{id}, а також відповідь на POST і PUT).
 */
public record FlowerDetailDto(Long id, String name, String category, String color, BigDecimal price,
                              Integer stemsCount, String season, String photoUrl,
                              String description) {
}
