package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для авторизации")
public class LoginDto {
    @JsonProperty("username")
    @Schema(description = "Логин", example = "user1", minLength = 4, maxLength = 32)
    private String username;

    @JsonProperty("password")
    @Schema(description = "Пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String password;
}

