package com.ua.flower.shop.exception;

/**
 * Виняток «квітку не знайдено».
 * Код 404 і тіло відповіді формує GlobalExceptionHandler.
 */
public class FlowerNotFoundException extends RuntimeException {

    public FlowerNotFoundException(Long id) {
        super("Квітку з id=" + id + " не знайдено");
    }
}
