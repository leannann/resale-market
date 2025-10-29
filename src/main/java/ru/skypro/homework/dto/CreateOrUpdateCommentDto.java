package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для создания/обновления комментария")
public class CreateOrUpdateCommentDto {
    @JsonProperty("text")
    @Schema(description = "Текст комментария", example = "Готов купить завтра", minLength = 8, maxLength = 64, required = true)
    private String text;
}
