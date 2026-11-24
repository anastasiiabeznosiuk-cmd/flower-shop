package com.ua.flower.shop.web;

import com.ua.flower.shop.exception.FlowerNotFoundException;
import com.ua.flower.shop.service.FlowerService;
import com.ua.flower.shop.web.dto.FlowerCardDto;
import com.ua.flower.shop.web.dto.FlowerDetailDto;
import com.ua.flower.shop.web.dto.FlowerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Інтеграційний тест-зріз (slice) веб-шару.
 * @WebMvcTest піднімає лише контролер, GlobalExceptionHandler і Spring MVC — без сервісу й репозиторію.
 * Сервіс замінено заглушкою (@MockBean), бо тут перевіряємо саме контролер:
 * адреси, коди відповіді, JSON, валідацію й формат помилок.
 * MockMvc надсилає запити без запуску справжнього сервера.
 */
@WebMvcTest(FlowerController.class)
class FlowerControllerTest {

    private static final String VALID_BODY = """
            {"name":"Ромашки польові","category":"букет","color":"білий","price":450.00,
             "stemsCount":21,"season":"літо","photoUrl":"/photos/chamomile.jpg","description":"Опис"}
            """;

    // порожня назва і від'ємна ціна → дві помилки валідації
    private static final String INVALID_BODY = """
            {"name":"","category":"букет","price":-5}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlowerService flowerService;   // сервіс замокано навмисно

    // ---------- GET ----------

    @Test
    void list_returnsFlowers() throws Exception {
        when(flowerService.findAll(isNull())).thenReturn(List.of(
                new FlowerCardDto(1L, "Троянда Red Naomi", "букет", "червоний", new BigDecimal("1850.00"), "/photos/red-naomi.jpg"),
                new FlowerCardDto(2L, "Тюльпани Strong Gold", "букет", "жовтий", new BigDecimal("950.00"), "/photos/strong-gold.jpg")));

        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Троянда Red Naomi"));
    }

    @Test
    void list_passesCategoryToService() throws Exception {
        when(flowerService.findAll("букет")).thenReturn(List.of());

        mockMvc.perform(get("/api/flowers").param("category", "букет"))
                .andExpect(status().isOk());

        verify(flowerService).findAll("букет");   // @RequestParam передав категорію в сервіс
    }

    @Test
    void getOne_returnsFlowerDetail() throws Exception {
        when(flowerService.findById(1L)).thenReturn(detail(1L));

        mockMvc.perform(get("/api/flowers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stemsCount").value(21));
    }

    @Test
    void getOne_returns404WhenMissing() throws Exception {
        when(flowerService.findById(999L)).thenThrow(new FlowerNotFoundException(999L));

        mockMvc.perform(get("/api/flowers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Квітку з id=999 не знайдено"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details").isEmpty());
    }

    // ---------- POST ----------

    @Test
    void create_returns201() throws Exception {
        when(flowerService.create(any(FlowerRequest.class))).thenReturn(detail(6L));

        mockMvc.perform(post("/api/flowers").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6));
    }

    @Test
    void create_returns400WhenBodyInvalid() throws Exception {
        mockMvc.perform(post("/api/flowers").contentType(MediaType.APPLICATION_JSON).content(INVALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details.length()").value(2));

        verify(flowerService, never()).create(any());   // до сервісу некоректні дані не дійшли
    }

    // ---------- PUT ----------

    @Test
    void update_returns200() throws Exception {
        when(flowerService.update(eq(6L), any(FlowerRequest.class))).thenReturn(detail(6L));

        mockMvc.perform(put("/api/flowers/6").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6));
    }

    @Test
    void update_returns404WhenMissing() throws Exception {
        when(flowerService.update(eq(999L), any(FlowerRequest.class))).thenThrow(new FlowerNotFoundException(999L));

        mockMvc.perform(put("/api/flowers/999").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void update_returns400WhenBodyInvalid() throws Exception {
        mockMvc.perform(put("/api/flowers/1").contentType(MediaType.APPLICATION_JSON).content(INVALID_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    // ---------- DELETE ----------

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/flowers/6"))
                .andExpect(status().isNoContent());

        verify(flowerService).delete(6L);
    }

    @Test
    void delete_returns404WhenMissing() throws Exception {
        doThrow(new FlowerNotFoundException(999L)).when(flowerService).delete(999L);

        mockMvc.perform(delete("/api/flowers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    private FlowerDetailDto detail(Long id) {
        return new FlowerDetailDto(id, "Ромашки польові", "букет", "білий", new BigDecimal("450.00"),
                21, "літо", "/photos/chamomile.jpg", "Опис");
    }
}
