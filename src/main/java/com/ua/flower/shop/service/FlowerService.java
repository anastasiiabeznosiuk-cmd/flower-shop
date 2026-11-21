package com.ua.flower.shop.service;

import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;

import java.util.List;

/**
 * Контракт сервісу: що вміє бізнес-логіка магазину.
 */
public interface FlowerService {

    List<FlowerCardDto> findAll();

    FlowerDetailDto findById(Long id);
}
