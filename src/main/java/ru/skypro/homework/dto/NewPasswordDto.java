package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * DTO для изменения пароля пользователя.
 * <p>
 * Используется при выполнении операции смены пароля авторизованного пользователя.
 * Требует указать действующий пароль и новый пароль. Длина обоих паролей
 * должна соответствовать требованиям валидации.
 */
@Data
@Schema(description = "Изменение пароля пользователя")
public class NewPasswordDto {

    /**
     * Текущий пароль пользователя.
     * <p>
     * Необходимо для проверки, что изменение выполняет владелец аккаунта.
     */
    @JsonProperty("currentPassword")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "Текущий пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String currentPassword;

    /**
     * Новый пароль пользователя.
     * <p>
     * Должен отличаться от текущего и соответствовать требованиям безопасности.
     */
    @JsonProperty("newPassword")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "Новый пароль", example = "Pass5678", minLength = 8, maxLength = 16)
    private String newPassword;
}

