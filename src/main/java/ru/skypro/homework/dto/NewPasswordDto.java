package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Изменение пароля пользователя")
public class NewPasswordDto {
    @JsonProperty("currentPassword")
    @Schema(description = "Текущий пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String currentPassword;

    @JsonProperty("newPassword")
    @Schema(description = "Новый пароль", example = "Pass5678", minLength = 8, maxLength = 16)
    private String newPassword;
}
