package com.ua.flower.shop.domain;

import java.math.BigDecimal;

/**
 * Доменна модель: квітка (букет) у магазині.
 * Звичайний Java-клас без анотацій Spring.
 */
public class Flower {

    private Long id;
    private String name;          // назва, напр. "Троянда Red Naomi"
    private String color;         // колір
    private BigDecimal price;     // ціна букета, грн
    private Integer stemsCount;   // кількість стебел у букеті
    private String season;        // сезон цвітіння
    private String photoUrl;      // фото (для картки)
    private String description;   // опис (для сторінки квітки)

    public Flower() {
    }

    public Flower(Long id, String name, String color, BigDecimal price,
                  Integer stemsCount, String season, String photoUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
        this.stemsCount = stemsCount;
        this.season = season;
        this.photoUrl = photoUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStemsCount() {
        return stemsCount;
    }

    public void setStemsCount(Integer stemsCount) {
        this.stemsCount = stemsCount;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
