package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO, представляющий коллекцию комментариев к объявлению.
 * <p>
 * Используется для передачи списка комментариев вместе с общим количеством.
 * Применяется в ответах API для удобства отображения комментариев на фронтенде.
 */
@Data
@Schema(description = "Список комментариев")
public class CommentsDto {

    /**
     * Общее количество комментариев.
     * <p>
     * Может не совпадать с размером списка, если используется пагинация (опционально).
     */
    @JsonProperty("count")
    @Schema(description = "Общее количество комментариев", example = "0")
    private Integer count;

    /**
     * Список комментариев.
     * <p>
     * Каждый элемент представлен объектом {@link CommentDto}.
     */
    @JsonProperty("results")
    @Schema(description = "Список комментариев")
    private List<CommentDto> results;
}
