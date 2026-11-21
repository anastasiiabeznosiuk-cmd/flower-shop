package com.ua.flower.shop.web.dto;

import java.math.BigDecimal;

/**
 * Вхідний DTO: дані, які клієнт надсилає в тілі запиту POST і PUT.
 * Поля id тут немає: його призначає сервер (при створенні)
 * або він береться з адреси запиту (при оновленні).
 */
public record FlowerRequest(String name, String category, String color, BigDecimal price,
                            Integer stemsCount, String season, String photoUrl,
                            String description) {
}
