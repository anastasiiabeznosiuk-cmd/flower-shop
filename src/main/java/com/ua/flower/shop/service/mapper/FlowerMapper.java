package com.ua.flower.shop.service.mapper;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import org.springframework.stereotype.Component;

/**
 * Маппер: перетворює доменну квітку на DTO, які віддаються назовні.
 */
@Component
public class FlowerMapper {

    public FlowerCardDto toCard(Flower f) {
        return new FlowerCardDto(f.getId(), f.getName(), f.getColor(),
                f.getPrice(), f.getPhotoUrl());
    }

    public FlowerDetailDto toDetail(Flower f) {
        return new FlowerDetailDto(f.getId(), f.getName(), f.getColor(), f.getPrice(),
                f.getStemsCount(), f.getSeason(), f.getPhotoUrl(), f.getDescription());
    }
}
