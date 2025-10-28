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
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы для работы с комментариями к объявлениям")
public class CommentsController {

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
        log.info("GET /ads/{}/comments", adId);
        CommentsDto body = new CommentsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
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
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCommentDto request) {
        log.info("POST /ads/{}/comments body={}", adId, request);
        return ResponseEntity.ok(new CommentDto());
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
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId) {
        log.info("DELETE /ads/{}/comments/{}", adId, commentId);
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
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateCommentDto request) {
        log.info("PATCH /ads/{}/comments/{} body={}", adId, commentId, request);
        return ResponseEntity.ok(new CommentDto());
    }
}
