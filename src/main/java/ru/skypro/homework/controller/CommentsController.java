package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.comment.CommentService;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы для работы с комментариями к объявлениям")
public class CommentsController {
    private final CommentService commentService;

    @Operation(
            summary = "Получение комментариев объявления",
            description = "Возвращает список всех комментариев для конкретного объявления.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарии успешно получены",
                            content = @Content(schema = @Schema(implementation = CommentsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @GetMapping("/ads/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(
            @Parameter(description = "ID объявления") @PathVariable("id") Integer adId) {
        log.info("GET /ads/{}/comments - получение комментариев", adId);

        List<CommentDto> comments = commentService.getCommentsByAdId(adId);

        CommentsDto response = new CommentsDto();
        response.setCount(comments.size());
        response.setResults(comments);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Добавление комментария",
            description = "Добавляет комментарий к объявлению. Возвращает созданный комментарий.",
            requestBody = @RequestBody(
                    description = "Данные нового комментария",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateCommentDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                            content = @Content(schema = @Schema(implementation = CommentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @PostMapping("/ads/{id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @Parameter(description = "ID объявления") @PathVariable("id") Integer adId,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCommentDto request, Authentication authentication) {


        String username = (authentication != null) ? authentication.getName() : null;
        log.info("POST /ads/{}/comments - добавление комментария пользователем: {}", adId, username);

        CommentDto comment = commentService.addComment(adId, request, username);

        return ResponseEntity.ok(comment);
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Удаляет комментарий по его ID. Доступно только автору или администратору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно удалён"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на удаление комментария"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            }
    )
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId,
            Authentication authentication) {
        log.info("DELETE /ads/{}/comments/{} - удаление комментария пользователем: {}", adId, commentId, authentication.getName());

        commentService.deleteComment(commentId, authentication.getName());

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление комментария",
            description = "Изменяет текст комментария по его ID.",
            requestBody = @RequestBody(
                    description = "Новый текст комментария",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateCommentDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлён",
                            content = @Content(schema = @Schema(implementation = CommentDto.class))),
                    @ApiResponse(responseCode = "403", description = "Нет прав на редактирование комментария"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            }
    )
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCommentDto request,
            Authentication authentication) {
        log.info("PATCH /ads/{}/comments/{} - обновление комментария пользователем: {}", adId, commentId, authentication.getName());

        CommentDto updatedComment = commentService.updateComment(commentId, request, authentication.getName());

        return ResponseEntity.ok(updatedComment);
    }
}
