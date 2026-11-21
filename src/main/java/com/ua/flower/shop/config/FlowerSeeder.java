package com.ua.flower.shop.config;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.repository.FlowerRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Сидер: під час запуску програми додає у сховище початкові квіти.
 * POST-ендпоінта ще немає, тому дані додаються тут, у коді.
 */
@Component
public class FlowerSeeder implements ApplicationRunner {

    private final FlowerRepository repository;

    public FlowerSeeder(FlowerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        repository.save(new Flower(null, "Троянда Red Naomi", "червоний",
                new BigDecimal("1850.00"), 25, "цілий рік", "/photos/red-naomi.jpg",
                "Класичний букет із 25 насичено-червоних троянд з великим бутоном. Стоїть у вазі до 10 днів."));

        repository.save(new Flower(null, "Тюльпани Strong Gold", "жовтий",
                new BigDecimal("950.00"), 31, "весна", "/photos/strong-gold.jpg",
                "Яскравий весняний букет із 31 жовтого тюльпана. Чудовий подарунок на 8 Березня."));

        repository.save(new Flower(null, "Півонії Sarah Bernhardt", "ніжно-рожевий",
                new BigDecimal("2400.00"), 15, "травень–червень", "/photos/sarah-bernhardt.jpg",
                "Пишні ароматні півонії, що розкриваються за 2–3 дні. Доступні лише в сезон."));

        repository.save(new Flower(null, "Хризантеми кущові", "білий",
                new BigDecimal("700.00"), 9, "осінь", "/photos/chrysanthemum-white.jpg",
                "Невибагливий осінній букет: на кожному стеблі кілька суцвіть. Дуже довго стоїть."));

        repository.save(new Flower(null, "Гортензія Magical", "блакитний",
                new BigDecimal("1300.00"), 5, "літо", "/photos/hydrangea-magical.jpg",
                "Великі блакитні суцвіття гортензії. Любить багато води — підрізайте стебла щодня."));
    }
}
