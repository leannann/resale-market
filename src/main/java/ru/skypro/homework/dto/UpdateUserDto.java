package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для обновления профиля пользователя")
public class UpdateUserDto {
    @JsonProperty("firstName")
    @Schema(description = "Имя", example = "Иван", minLength = 3, maxLength = 10)
    private String firstName;

    @JsonProperty("lastName")
    @Schema(description = "Фамилия", example = "Иванов", minLength = 3, maxLength = 10)
    private String lastName;

    @JsonProperty("phone")
    @Schema(description = "Телефон", example = "+7 (900) 111-22-33")
    private String phone;
}

