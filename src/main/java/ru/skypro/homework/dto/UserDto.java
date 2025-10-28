package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Профиль пользователя")
public class UserDto {
    @JsonProperty("id")
    @Schema(description = "ID пользователя", example = "12")
    private Integer id;

    @JsonProperty("email")
    @Schema(description = "Логин пользователя (email)", example = "user1@example.com")
    private String email;

    @JsonProperty("firstName")
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @JsonProperty("lastName")
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @JsonProperty("phone")
    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    private String phone;

    @JsonProperty("role")
    @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN"})
    private Role role;

    @JsonProperty("image")
    @Schema(description = "Ссылка на аватар пользователя", example = "/images/avatars/12.jpg")
    private String avatarUrl;
}
