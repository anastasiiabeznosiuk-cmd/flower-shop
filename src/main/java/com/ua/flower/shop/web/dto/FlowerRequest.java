package com.ua.flower.shop.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Вхідний DTO: дані, які клієнт надсилає в тілі запиту POST і PUT.
 * Поля id тут немає: його призначає сервер (при створенні)
 * або він береться з адреси запиту (при оновленні).
 * Анотації валідації перевіряються, коли в контролері стоїть @Valid.
 */
public record FlowerRequest(
        @NotBlank(message = "Назва не може бути порожньою")
        String name,

        @NotBlank(message = "Категорія не може бути порожньою")
        String category,

        String color,

        @NotNull(message = "Ціна обов'язкова")
        @Positive(message = "Ціна має бути більшою за нуль")
        BigDecimal price,

        @Positive(message = "Кількість стебел має бути більшою за нуль")
        Integer stemsCount,

        String season,
        String photoUrl,
        String description) {
}
