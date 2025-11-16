package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, представляющий данные, необходимые для авторизации пользователя.
 * <p>
 * Используется в процессе входа в систему (логин).
 * Содержит логин (username/email) и пароль.
 * Поля должны соответствовать требованиям на стороне сервера.
 */
@Data
@Schema(description = "Данные для авторизации")
public class LoginDto {

    /**
     * Логин пользователя (username).
     * <p>
     * Обычно соответствует email, использованному при регистрации.
     */
    @JsonProperty("username")
    @Schema(description = "Логин", example = "user1", minLength = 4, maxLength = 32)
    private String username;

    /**
     * Пароль пользователя.
     * <p>
     * Перед сравнением проходит проверку в PasswordEncoder.
     */
    @JsonProperty("password")
    @Schema(description = "Пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String password;
}


