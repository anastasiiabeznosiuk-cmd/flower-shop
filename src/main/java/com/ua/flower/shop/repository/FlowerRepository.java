package com.ua.flower.shop.repository;

import com.ua.flower.shop.domain.Flower;

import java.util.List;
import java.util.Optional;

/**
 * Контракт сховища квітів: що вміє «комора».
 */
public interface FlowerRepository {

    List<Flower> findAll();

    List<Flower> findByCategory(String category);

    Optional<Flower> findById(Long id);

    Flower save(Flower flower);

    /** Повертає true, якщо квітку видалено, і false, якщо такої не було. */
    boolean deleteById(Long id);
}
