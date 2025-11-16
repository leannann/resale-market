package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления профиля пользователя.
 * <p>
 * Используется при редактировании информации о пользователе (имя, фамилия, телефон).
 * Все поля необязательные — изменяются только те, что переданы в запросе.
 */
@Data
@Schema(description = "Данные для обновления профиля пользователя")
public class UpdateUserDto {

    /**
     * Новое имя пользователя.
     * Если значение не указано, имя не изменяется.
     */
    @JsonProperty("firstName")
    @Schema(description = "Имя", example = "Иван", minLength = 3, maxLength = 10)
    private String firstName;

    /**
     * Новая фамилия пользователя.
     * Если значение не указано, фамилия не изменяется.
     */
    @JsonProperty("lastName")
    @Schema(description = "Фамилия", example = "Иванов", minLength = 3, maxLength = 10)
    private String lastName;

    /**
     * Новый номер телефона пользователя.
     * Если значение не указано, телефон не изменяется.
     */
    @JsonProperty("phone")
    @Schema(description = "Телефон", example = "+7 (900) 111-22-33")
    private String phone;
}


