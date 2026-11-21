package com.ua.flower.shop.web;

import com.ua.flower.shop.service.FlowerService;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контролер: приймає HTTP-запити за адресою /api/flowers.
 */
@RestController
@RequestMapping("/api/flowers")
public class FlowerController {

    private final FlowerService flowerService;

    public FlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    // GET /api/flowers — перелік квітів
    @GetMapping
    public List<FlowerCardDto> list() {
        return flowerService.findAll();
    }

    // GET /api/flowers/{id} — одна квітка; якщо немає → 404
    @GetMapping("/{id}")
    public FlowerDetailDto getOne(@PathVariable Long id) {
        return flowerService.findById(id);
    }
}
