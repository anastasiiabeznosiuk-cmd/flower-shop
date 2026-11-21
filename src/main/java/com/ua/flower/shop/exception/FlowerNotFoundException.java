package com.ua.flower.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток «квітку не знайдено». Spring перетворює його на відповідь 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FlowerNotFoundException extends RuntimeException {

    public FlowerNotFoundException(Long id) {
        super("Квітку з id=" + id + " не знайдено");
    }
}
