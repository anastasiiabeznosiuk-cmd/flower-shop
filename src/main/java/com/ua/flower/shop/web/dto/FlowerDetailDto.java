package com.ua.flower.shop.web.dto;

import java.math.BigDecimal;

/**
 * Повна інформація про квітку для її сторінки: GET /api/flowers/{id}
 */
public record FlowerDetailDto(Long id, String name, String color, BigDecimal price,
                              Integer stemsCount, String season, String photoUrl,
                              String description) {
}
