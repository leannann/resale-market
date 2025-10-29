package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void setPassword_ok() throws Exception {
        NewPasswordDto dto = new NewPasswordDto();
        dto.setCurrentPassword("Pass1234");
        dto.setNewPassword("Pass5678");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getMe_ok() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateMe_ok() throws Exception {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setFirstName("Иван");
        dto.setLastName("Иванов");
        dto.setPhone("+7 (900) 111-22-33");

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 111-22-33"));
    }

    @Test
    @WithMockUser
    void updateAvatar_ok() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "avatar.jpg", "image/jpeg", new byte[]{1,2,3}
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void ping_ok() throws Exception {
        mockMvc.perform(get("/users/ping"))
                .andExpect(status().isOk());
    }
}
