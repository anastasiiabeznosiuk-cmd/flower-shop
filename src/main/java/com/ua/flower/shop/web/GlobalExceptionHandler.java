package com.ua.flower.shop.web;

import com.ua.flower.shop.exception.FlowerNotFoundException;
import com.ua.flower.shop.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Глобальна обробка помилок: перехоплює винятки з усіх контролерів
 * і повертає їх в одному форматі ErrorResponse (JSON).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Квітку не знайдено → 404
    @ExceptionHandler(FlowerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(FlowerNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage(), List.of());
    }

    // Тіло запиту не пройшло валідацію (@Valid) → 400 з переліком проблем
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .toList();
        return new ErrorResponse(400, "Bad Request", "Дані запиту не пройшли перевірку", details);
    }
}
