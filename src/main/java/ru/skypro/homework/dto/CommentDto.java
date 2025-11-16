package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, представляющий комментарий к объявлению.
 * <p>
 * Используется для передачи данных комментариев на фронтенд.
 * Включает информацию об авторе, времени создания и тексте комментария.
 * Служит отображением сущности {@link ru.skypro.homework.entity.Comment}.
 */
@Data
@Schema(description = "Комментарий к объявлению")
public class CommentDto {

    /**
     * Идентификатор автора комментария.
     */
    @JsonProperty("author")
    @Schema(description = "ID автора комментария", example = "12")
    private Integer author;

    /**
     * Ссылка на аватар автора комментария.
     * <p>
     * Как правило, локальный URL, формируемый ImageService.
     */
    @JsonProperty("authorImage")
    @Schema(description = "Ссылка на аватар автора комментария", example = "/images/avatars/12.jpg")
    private String authorImage;

    /**
     * Имя автора комментария.
     */
    @JsonProperty("authorFirstName")
    @Schema(description = "Имя автора комментария", example = "Иван")
    private String authorFirstName;

    /**
     * Время создания комментария в формате Unix Epoch (миллисекунды).
     */
    @JsonProperty("createdAt")
    @Schema(description = "Дата/время создания (epoch millis с 01.01.1970 00:00:00 UTC)",
            example = "1698672000000")
    private Long createdAt;

    /**
     * Уникальный идентификатор комментария.
     */
    @JsonProperty("pk")
    @Schema(description = "ID комментария", example = "501")
    private Integer pk;

    /**
     * Текст комментария.
     */
    @JsonProperty("text")
    @Schema(description = "Текст комментария", example = "Отличное состояние!",
            minLength = 8, maxLength = 64)
    private String text;
}

