package com.ua.flower.shop.web.dto;

import java.util.List;

/**
 * Єдиний формат тіла відповіді для всіх помилок API (404, 400).
 *
 * @param status  числовий код, напр. 404
 * @param error   назва коду, напр. "Not Found"
 * @param message зрозуміле пояснення
 * @param details перелік проблем (для 400 — по одній на кожне поле; для 404 — порожній)
 */
public record ErrorResponse(int status, String error, String message, List<String> details) {
}
