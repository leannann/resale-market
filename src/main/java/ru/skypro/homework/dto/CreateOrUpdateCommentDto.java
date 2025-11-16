package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, используемый для создания нового комментария
 * или обновления существующего.
 * <p>
 * Содержит только текст комментария, так как остальные данные
 * (автор, объявление, время создания) определяются на сервере.
 */
@Data
@Schema(description = "Данные для создания/обновления комментария")
public class CreateOrUpdateCommentDto {

    /**
     * Текст комментария.
     * <p>
     * Должен соответствовать требованиям по длине, заданным в спецификации.
     */
    @JsonProperty("text")
    @Schema(
            description = "Текст комментария",
            example = "Готов купить завтра",
            minLength = 8,
            maxLength = 64,
            required = true
    )
    private String text;
}

