package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, содержащий информацию о профиле пользователя.
 * <p>
 * Используется для передачи данных пользователя на фронтенд.
 * Включает идентификатор, контактные данные, роль и ссылку на аватар.
 */
@Data
@Schema(description = "Профиль пользователя")
public class UserDto {

    /**
     * Уникальный идентификатор пользователя.
     */
    @JsonProperty("id")
    @Schema(description = "ID пользователя", example = "12")
    private Integer id;

    /**
     * Email пользователя — используется как логин.
     */
    @JsonProperty("email")
    @Schema(description = "Логин пользователя (email)", example = "user1@example.com")
    private String email;

    /**
     * Имя пользователя.
     */
    @JsonProperty("firstName")
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @JsonProperty("lastName")
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    /**
     * Телефон пользователя.
     */
    @JsonProperty("phone")
    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    private String phone;

    /**
     * Роль пользователя: USER или ADMIN.
     */
    @JsonProperty("role")
    @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN"})
    private Role role;

    /**
     * Ссылка на аватар пользователя (локальный URL к загруженному изображению).
     */
    @JsonProperty("image")
    @Schema(description = "Ссылка на аватар пользователя", example = "/images/avatars/12.jpg")
    private String image;
}

