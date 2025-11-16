package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Schema(description = "Изменение пароля пользователя")
public class NewPasswordDto {
    @JsonProperty("currentPassword")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "Текущий пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String currentPassword;

    @JsonProperty("newPassword")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "Новый пароль", example = "Pass5678", minLength = 8, maxLength = 16)
    private String newPassword;
}
