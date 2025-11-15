package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.service.add.AdService;
import ru.skypro.homework.service.image.ImageService;


import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    void getAllAds_ok() throws Exception {
        AdsDto mockAdsDto = new AdsDto();
        mockAdsDto.setCount(0);
        mockAdsDto.setResults(new ArrayList<>());

        // Мокаем вызов сервиса
        when(adService.getAllAds()).thenReturn(mockAdsDto);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
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

        mockMvc.perform(multipart("/ads").file(props).file(image).with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addAd_withoutImage_badRequest() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Велосипед");
        dto.setPrice(10000);

        MockMultipartFile props = new MockMultipartFile(
                "properties", "properties.json", "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
        // Нет файла изображения

        // Мокаем выброс исключения при отсутствии изображения
        when(adService.createAd(any(CreateOrUpdateAdDto.class), any(MultipartFile.class), any(String.class)))
                .thenThrow(new IllegalArgumentException("Изображение обязательно"));

        mockMvc.perform(multipart("/ads").file(props).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addAd_withEmptyTitle_badRequest() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle(""); // Пустой заголовок
        dto.setPrice(10000);

        MockMultipartFile props = new MockMultipartFile(
                "properties", "properties.json", "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
        MockMultipartFile image = new MockMultipartFile(
                "image", "bike.jpg", "image/jpeg", new byte[]{1,2,3}
        );

        // Мокаем выброс исключения при пустом заголовке
        when(adService.createAd(any(CreateOrUpdateAdDto.class), any(MultipartFile.class), any(String.class)))
                .thenThrow(new IllegalArgumentException("Заголовок объявления не может быть пустым"));

        mockMvc.perform(multipart("/ads").file(props).file(image).with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAdById_ok() throws Exception {
        mockMvc.perform(get("/ads/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAd_asOwner_ok() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Новый заголовок");
        dto.setPrice(20000);
        dto.setDescription("Обновлённое описание");

        // Мокаем проверку прав - пользователь является владельцем
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(true);

        mockMvc.perform(patch("/ads/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAd_asAdmin_ok() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Новый заголовок");
        dto.setPrice(20000);
        dto.setDescription("Обновлённое описание");

        // Админ может обновлять даже если не владелец
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        mockMvc.perform(patch("/ads/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAd_asNotOwner_forbidden() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Новый заголовок");
        dto.setPrice(20000);

        // Пользователь не является владельцем и не админ
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        // Мокаем выброс исключения при попытке обновления
        when(adService.updateAd(eq(1), any(CreateOrUpdateAdDto.class), any(String.class)))
                .thenThrow(new RuntimeException("Нет прав для обновления объявления"));

        mockMvc.perform(patch("/ads/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAd_asAdmin_noContent() throws Exception {
        // Админ может удалять любые объявления
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        // Мокаем метод delete - он ничего не возвращает (void)
        doNothing().when(adService).deleteAd(eq(1), any(String.class));

        mockMvc.perform(delete("/ads/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteAd_asOwner_noContent() throws Exception {
        // Владелец может удалять свои объявления
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(true);

        mockMvc.perform(delete("/ads/{id}", 1).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteAd_asNotOwner_forbidden() throws Exception {
        // Обычный пользователь не может удалять чужие объявления
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        // Мокаем выброс исключения при попытке удаления
        doThrow(new RuntimeException("Нет прав для удаления объявления"))
                .when(adService).deleteAd(eq(1), any(String.class));

        mockMvc.perform(delete("/ads/{id}", 1).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateImage_asOwner_ok() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", new byte[]{5,6,7}
        );

        // Владелец может обновлять изображение
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(true);

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(csrf())
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateImage_asAdmin_ok() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", new byte[]{5,6,7}
        );

        // Админ может обновлять изображение даже если не владелец
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(csrf())
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateImage_asNotOwner_forbidden() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", new byte[]{5,6,7}
        );

        // Обычный пользователь не может обновлять чужое изображение
        when(adService.isOwner(eq(1), any(String.class))).thenReturn(false);

        // Мокаем выброс исключения при попытке обновления изображения
        doThrow(new RuntimeException("Нет прав для обновления объявления"))
                .when(adService).updateAdImage(eq(1), any(MultipartFile.class), any(String.class));

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(csrf())
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getMyAds_ok() throws Exception {
        AdsDto mockAdsDto = new AdsDto();
        mockAdsDto.setCount(0);
        mockAdsDto.setResults(new ArrayList<>());

        // Мокаем вызов сервиса
        when(adService.getMyAds(any(String.class))).thenReturn(mockAdsDto);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }
}
