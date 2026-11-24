package com.ua.flower.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.flower.shop.repository.FlowerRepository;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Наскрізний (end-to-end) інтеграційний тест.
 * @SpringBootTest піднімає весь застосунок зі справжніми бінами — нічого не замокано:
 * запит проходить контролер → сервіс → маппер → репозиторій.
 * Тести автономні: перед кожним сховище очищується, і кожен тест сам створює свої дані.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FlowerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlowerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearState() {
        repository.deleteAll();   // чистий старт: стартові квіти сидера тестам не заважають
    }

    @Test
    void getAll_returnsExactlyWhatTestCreated() throws Exception {
        givenFlower("Троянда", "букет");
        givenFlower("Гортензія", "у горщику");

        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void filterByCategory_returnsOnlyMatching() throws Exception {
        givenFlower("Троянда", "букет");
        givenFlower("Гортензія", "у горщику");

        mockMvc.perform(get("/api/flowers").param("category", "букет"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Троянда"));
    }

    @Test
    void create_thenGetById_returnsCreatedFlower() throws Exception {
        long id = givenFlower("Півонії", "композиція");

        mockMvc.perform(get("/api/flowers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Півонії"));
    }

    @Test
    void update_changesFlower() throws Exception {
        long id = givenFlower("Ромашки", "букет");
        FlowerRequest changed = new FlowerRequest("Ромашки", "букет", "білий", new BigDecimal("500.00"),
                25, "літо", "/photos/chamomile.jpg", "Збільшений букет");

        mockMvc.perform(put("/api/flowers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changed)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stemsCount").value(25));

        mockMvc.perform(get("/api/flowers/{id}", id))
                .andExpect(jsonPath("$.description").value("Збільшений букет"));
    }

    @Test
    void delete_thenGet_returns404() throws Exception {
        long id = givenFlower("Тюльпани", "букет");

        mockMvc.perform(delete("/api/flowers/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/flowers/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void update_missingFlower_returns404() throws Exception {
        long id = givenFlower("Хризантеми", "букет");
        mockMvc.perform(delete("/api/flowers/{id}", id));   // тепер квітки з цим id точно немає

        mockMvc.perform(put("/api/flowers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("Хризантеми", "букет"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_invalidBody_returns400WithDetails() throws Exception {
        FlowerRequest invalid = new FlowerRequest("", "букет", null, new BigDecimal("-5"),
                null, null, null, null);

        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details.length()").value(2));
    }

    // ---- допоміжні методи ----

    /** Створює квітку через API (POST) і повертає її справжній id. */
    private long givenFlower(String name, String category) throws Exception {
        String response = mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request(name, category))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readTree(response).get("id").asLong();
    }

    private FlowerRequest request(String name, String category) {
        return new FlowerRequest(name, category, "білий", new BigDecimal("450.00"),
                21, "літо", "/photos/test.jpg", "Тестова квітка");
    }
}
