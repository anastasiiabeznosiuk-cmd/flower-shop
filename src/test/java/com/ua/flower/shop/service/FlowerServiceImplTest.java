package com.ua.flower.shop.service;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.exception.FlowerNotFoundException;
import com.ua.flower.shop.repository.FlowerRepository;
import com.ua.flower.shop.service.mapper.FlowerMapper;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Модульний (unit) тест сервісу.
 * Spring не запускається: сервіс збираємо вручну через конструктор.
 * Репозиторій — mock-об'єкт (заглушка) Mockito, маппер — справжній.
 */
class FlowerServiceImplTest {

    private final FlowerRepository repository = mock(FlowerRepository.class);
    private final FlowerMapper mapper = new FlowerMapper();
    private final FlowerService service = new FlowerServiceImpl(repository, mapper);

    @Test
    void findById_returnsFlower() {
        when(repository.findById(1L)).thenReturn(Optional.of(rose(1L)));

        FlowerDetailDto result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Троянда Red Naomi");
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(FlowerNotFoundException.class)
                .hasMessage("Квітку з id=999 не знайдено");
    }

    @Test
    void findAll_withoutCategory_returnsAllFlowers() {
        when(repository.findAll()).thenReturn(List.of(rose(1L), rose(2L)));

        List<FlowerCardDto> result = service.findAll(null);

        assertThat(result).hasSize(2);
        verify(repository, never()).findByCategory(any());
    }

    @Test
    void findAll_withCategory_usesFilter() {
        when(repository.findByCategory("букет")).thenReturn(List.of(rose(1L)));

        List<FlowerCardDto> result = service.findAll("букет");

        assertThat(result).hasSize(1);
        verify(repository).findByCategory("букет");
        verify(repository, never()).findAll();
    }

    @Test
    void create_savesFlowerAndReturnsItWithNewId() {
        // заглушка поводиться як справжнє сховище: призначає id і повертає квітку
        when(repository.save(any(Flower.class))).thenAnswer(invocation -> {
            Flower saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        FlowerDetailDto result = service.create(request());

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Ромашки польові");
    }

    @Test
    void update_throwsWhenNotFound_andDoesNotSave() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, request()))
                .isInstanceOf(FlowerNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_removesExistingFlower() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenNothingDeleted() {
        when(repository.deleteById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(FlowerNotFoundException.class);
    }

    // ---- допоміжні методи: тестові дані ----

    private Flower rose(Long id) {
        return new Flower(id, "Троянда Red Naomi", "букет", "червоний", new BigDecimal("1850.00"),
                25, "цілий рік", "/photos/red-naomi.jpg", "Класичний букет троянд.");
    }

    private FlowerRequest request() {
        return new FlowerRequest("Ромашки польові", "букет", "білий", new BigDecimal("450.00"),
                21, "літо", "/photos/chamomile.jpg", "Легкий літній букет.");
    }
}
