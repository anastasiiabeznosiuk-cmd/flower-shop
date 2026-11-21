package com.ua.flower.shop.service.mapper;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.springframework.stereotype.Component;

/**
 * Маппер: перетворює доменну квітку на DTO і назад.
 */
@Component
public class FlowerMapper {

    public FlowerCardDto toCard(Flower f) {
        return new FlowerCardDto(f.getId(), f.getName(), f.getCategory(), f.getColor(),
                f.getPrice(), f.getPhotoUrl());
    }

    public FlowerDetailDto toDetail(Flower f) {
        return new FlowerDetailDto(f.getId(), f.getName(), f.getCategory(), f.getColor(), f.getPrice(),
                f.getStemsCount(), f.getSeason(), f.getPhotoUrl(), f.getDescription());
    }

    /** Вхідний DTO → доменна квітка. id не встановлюємо: це робить сервіс. */
    public Flower toDomain(FlowerRequest r) {
        return new Flower(null, r.name(), r.category(), r.color(), r.price(),
                r.stemsCount(), r.season(), r.photoUrl(), r.description());
    }
}
