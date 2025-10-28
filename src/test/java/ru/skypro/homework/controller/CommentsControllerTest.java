package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsController.class)
class CommentsControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getComments_ok() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void addComment_ok() throws Exception {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("Отличное состояние!");

        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateComment_ok() throws Exception {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("Исправленный текст комментария");

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteComment_ok() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 2))
                .andExpect(status().isOk());
    }
}
