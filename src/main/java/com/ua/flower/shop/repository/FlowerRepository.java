package com.ua.flower.shop.repository;

import com.ua.flower.shop.domain.Flower;

import java.util.List;
import java.util.Optional;

/**
 * Контракт сховища квітів: що вміє «комора».
 */
public interface FlowerRepository {

    List<Flower> findAll();

    Optional<Flower> findById(Long id);

    Flower save(Flower flower);
}
