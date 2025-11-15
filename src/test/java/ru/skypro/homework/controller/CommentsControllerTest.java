package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.comment.CommentService;


import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsController.class)
@Import(TestSecurityConfig.class)
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void getComments_ok() throws Exception {
        List<CommentDto> comments = List.of(
                createCommentDto(1, "Отличный товар!"),
                createCommentDto(2, "Быстрая доставка")
        );
        CommentsDto response = new CommentsDto();
        response.setCount(comments.size());
        response.setResults(comments);

        when(commentService.getCommentsByAdId(1)).thenReturn(comments);

        mockMvc.perform(get("/ads/{id}/comments", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0].text").value("Отличный товар!"));
    }

    @Test
    @WithMockUser
    void addComment_ok() throws Exception {
        CreateOrUpdateCommentDto request = new CreateOrUpdateCommentDto();
        request.setText("Отличное состояние!");

        CommentDto response = createCommentDto(1, "Отличное состояние!");

        when(commentService.addComment(eq(1), any(CreateOrUpdateCommentDto.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Отличное состояние!"));
    }

    @Test
    @WithMockUser
    void updateComment_ok() throws Exception {
        CreateOrUpdateCommentDto request = new CreateOrUpdateCommentDto();
        request.setText("Исправленный текст комментария");

        CommentDto response = createCommentDto(2, "Исправленный текст комментария");

        when(commentService.updateComment(eq(2), any(CreateOrUpdateCommentDto.class), anyString()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Исправленный текст комментария"));
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    void deleteComment_ok() throws Exception {
        doNothing().when(commentService).deleteComment(2, "user@mail.com");

        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 2))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getComments_adNotFound() throws Exception {
        when(commentService.getCommentsByAdId(999))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Объявление не найдено"));

        mockMvc.perform(get("/ads/{id}/comments", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void addComment_unauthorized() throws Exception {
        CreateOrUpdateCommentDto request = new CreateOrUpdateCommentDto();
        request.setText("Тестовый комментарий");

        // Настраиваем сервис, чтобы он бросал исключение для неаутентифицированного пользователя
        when(commentService.addComment(eq(1), any(CreateOrUpdateCommentDto.class), isNull()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не аутентифицирован"));

        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateOrUpdateCommentDto())))
                .andExpect(status().isUnauthorized());
    }

    private CommentDto createCommentDto(Integer id, String text) {
        CommentDto dto = new CommentDto();
        dto.setPk(id);
        dto.setText(text);
        dto.setAuthorFirstName("Иван");
        dto.setAuthorImage("/images/user1.jpg");
        dto.setCreatedAt(Instant.now().toEpochMilli());
        return dto;
    }
}
