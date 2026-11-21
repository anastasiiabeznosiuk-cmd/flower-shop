package com.ua.flower.shop.web;

import com.ua.flower.shop.service.FlowerService;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контролер: приймає HTTP-запити за адресою /api/flowers (повний CRUD).
 */
@RestController
@RequestMapping("/api/flowers")
public class FlowerController {

    private final FlowerService flowerService;

    public FlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    // GET /api/flowers                    — усі квіти (200)
    // GET /api/flowers?category=букет     — лише вказаної категорії (200)
    @GetMapping
    public List<FlowerCardDto> list(@RequestParam(required = false) String category) {
        return flowerService.findAll(category);
    }

    // GET /api/flowers/{id} — одна квітка (200) або 404
    @GetMapping("/{id}")
    public FlowerDetailDto getOne(@PathVariable Long id) {
        return flowerService.findById(id);
    }

    // POST /api/flowers — створити квітку з JSON у тілі запиту → 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlowerDetailDto create(@RequestBody FlowerRequest request) {
        return flowerService.create(request);
    }

    // PUT /api/flowers/{id} — повністю замінити квітку → 200 OK або 404
    @PutMapping("/{id}")
    public FlowerDetailDto update(@PathVariable Long id, @RequestBody FlowerRequest request) {
        return flowerService.update(id, request);
    }

    // DELETE /api/flowers/{id} — видалити квітку → 204 No Content або 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flowerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
