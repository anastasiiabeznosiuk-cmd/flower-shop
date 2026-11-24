package com.ua.flower.shop.service.mapper;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Модульний (unit) тест маппера: залежностей немає, тому й заглушки не потрібні.
 */
class FlowerMapperTest {

    private final FlowerMapper mapper = new FlowerMapper();

    @Test
    void toDomain_doesNotSetId() {
        FlowerRequest request = new FlowerRequest("Ромашки польові", "букет", "білий",
                new BigDecimal("450.00"), 21, "літо", "/photos/chamomile.jpg", "Опис");

        Flower flower = mapper.toDomain(request);

        assertThat(flower.getId()).isNull();          // id призначає сервер, а не клієнт
        assertThat(flower.getName()).isEqualTo("Ромашки польові");
        assertThat(flower.getCategory()).isEqualTo("букет");
    }

    @Test
    void toCard_copiesShortFields() {
        FlowerCardDto card = mapper.toCard(hydrangea());

        assertThat(card.id()).isEqualTo(5L);
        assertThat(card.name()).isEqualTo("Гортензія Magical");
        assertThat(card.price()).isEqualByComparingTo("1300.00");
    }

    @Test
    void toDetail_copiesAllFields() {
        FlowerDetailDto detail = mapper.toDetail(hydrangea());

        assertThat(detail.stemsCount()).isEqualTo(5);
        assertThat(detail.season()).isEqualTo("літо");
        assertThat(detail.description()).isEqualTo("Гортензія в горщику.");
    }

    private Flower hydrangea() {
        return new Flower(5L, "Гортензія Magical", "у горщику", "блакитний", new BigDecimal("1300.00"),
                5, "літо", "/photos/hydrangea-magical.jpg", "Гортензія в горщику.");
    }
}
