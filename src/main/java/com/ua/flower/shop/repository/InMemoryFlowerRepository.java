package com.ua.flower.shop.repository;

import com.ua.flower.shop.domain.Flower;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Реалізація сховища в пам'яті: квіти лежать у Map, поки працює програма.
 */
@Repository
public class InMemoryFlowerRepository implements FlowerRepository {

    private final Map<Long, Flower> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public List<Flower> findAll() {
        // сортуємо за id, щоб перелік завжди мав однаковий порядок
        return storage.values().stream()
                .sorted(Comparator.comparing(Flower::getId))
                .toList();
    }

    @Override
    public List<Flower> findByCategory(String category) {
        // фільтр без урахування регістру: "Букет" і "букет" — одне й те саме
        return storage.values().stream()
                .filter(f -> f.getCategory() != null && f.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Flower::getId))
                .toList();
    }

    @Override
    public Optional<Flower> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Flower save(Flower flower) {
        // нова квітка (id == null) отримує наступний номер; наявна — перезаписується
        if (flower.getId() == null) {
            flower.setId(sequence.incrementAndGet());
        }
        storage.put(flower.getId(), flower);
        return flower;
    }

    @Override
    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}
