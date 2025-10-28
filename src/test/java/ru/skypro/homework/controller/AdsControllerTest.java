package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
class AdsControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllAds_ok() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void addAd_created() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Велосипед");
        dto.setPrice(10000);
        dto.setDescription("Почти новый");

        MockMultipartFile props = new MockMultipartFile(
                "properties", "properties.json", "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
        MockMultipartFile image = new MockMultipartFile(
                "image", "bike.jpg", "image/jpeg",
                new byte[]{1,2,3}
        );

        mockMvc.perform(multipart("/ads").file(props).file(image))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.title").value("Велосипед"))
                .andExpect(jsonPath("$.price").value(10000));
    }

    @Test
    @WithMockUser
    void getAdById_ok() throws Exception {
        mockMvc.perform(get("/ads/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateAd_ok() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Новый заголовок");
        dto.setPrice(20000);
        dto.setDescription("Обновлённое описание");

        mockMvc.perform(patch("/ads/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteAd_noContent() throws Exception {
        mockMvc.perform(delete("/ads/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateImage_ok() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", new byte[]{5,6,7}
        );

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getMyAds_ok() throws Exception {
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }
}
