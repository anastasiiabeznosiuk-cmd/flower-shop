package com.ua.flower.shop.web.dto;

import java.math.BigDecimal;

/**
 * Коротка картка квітки для переліку: GET /api/flowers
 */
public record FlowerCardDto(Long id, String name, String color,
                            BigDecimal price, String photoUrl) {
}
