package com.ua.flower.shop.service;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.exception.FlowerNotFoundException;
import com.ua.flower.shop.repository.FlowerRepository;
import com.ua.flower.shop.service.mapper.FlowerMapper;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реалізація сервісу: бере квіти зі сховища й перетворює їх на DTO.
 */
@Service
public class FlowerServiceImpl implements FlowerService {

    private final FlowerRepository repository;
    private final FlowerMapper mapper;

    public FlowerServiceImpl(FlowerRepository repository, FlowerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<FlowerCardDto> findAll() {
        return repository.findAll().stream().map(mapper::toCard).toList();
    }

    @Override
    public FlowerDetailDto findById(Long id) {
        Flower flower = repository.findById(id)
                .orElseThrow(() -> new FlowerNotFoundException(id));
        return mapper.toDetail(flower);
    }
}
