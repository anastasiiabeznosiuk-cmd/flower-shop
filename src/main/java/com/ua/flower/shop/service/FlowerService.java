package com.ua.flower.shop.service;

import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;

import java.util.List;

/**
 * Контракт сервісу: що вміє бізнес-логіка магазину.
 */
public interface FlowerService {

    /** Усі квіти, або лише вказаної категорії, якщо category не порожня. */
    List<FlowerCardDto> findAll(String category);

    FlowerDetailDto findById(Long id);

    FlowerDetailDto create(FlowerRequest request);

    FlowerDetailDto update(Long id, FlowerRequest request);

    void delete(Long id);
}
