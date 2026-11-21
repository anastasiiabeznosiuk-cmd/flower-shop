package com.ua.flower.shop.service;

import com.ua.flower.shop.domain.Flower;
import com.ua.flower.shop.exception.FlowerNotFoundException;
import com.ua.flower.shop.repository.FlowerRepository;
import com.ua.flower.shop.service.mapper.FlowerMapper;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реалізація сервісу: бізнес-логіка для читання, створення, оновлення й видалення квітів.
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
    public List<FlowerCardDto> findAll(String category) {
        List<Flower> flowers = (category == null || category.isBlank())
                ? repository.findAll()
                : repository.findByCategory(category);
        return flowers.stream().map(mapper::toCard).toList();
    }

    @Override
    public FlowerDetailDto findById(Long id) {
        Flower flower = repository.findById(id)
                .orElseThrow(() -> new FlowerNotFoundException(id));
        return mapper.toDetail(flower);
    }

    @Override
    public FlowerDetailDto create(FlowerRequest request) {
        Flower flower = mapper.toDomain(request);
        flower.setId(null); // id призначає сервер (сховище)
        return mapper.toDetail(repository.save(flower));
    }

    @Override
    public FlowerDetailDto update(Long id, FlowerRequest request) {
        if (repository.findById(id).isEmpty()) {
            throw new FlowerNotFoundException(id); // 404
        }
        Flower flower = mapper.toDomain(request);
        flower.setId(id); // id беремо з адреси запиту
        return mapper.toDetail(repository.save(flower));
    }

    @Override
    public void delete(Long id) {
        if (repository.deleteById(id)) {
            return; // видалили → контролер поверне 204
        }
        throw new FlowerNotFoundException(id); // такої квітки не було → 404
    }
}
