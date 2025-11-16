package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, представляющий данные, необходимые для регистрации нового пользователя.
 * <p>
 * Используется в процессе создания нового аккаунта. Все поля обязательны,
 * кроме роли — если роль не указана, в сервисе ей присваивается значение USER.
 */
@Data
@Schema(description = "Данные для регистрации пользователя")
public class RegisterDto {

    /**
     * Логин пользователя. На этапе регистрации используется как email,
     * который затем сохраняется в базу данных в поле {@code email}.
     */
    @JsonProperty("username")
    @Schema(description = "Логин", example = "user1", minLength = 4, maxLength = 32)
    private String username;

    /**
     * Пароль пользователя. Перед сохранением обязательно шифруется
     * {@link org.springframework.security.crypto.password.PasswordEncoder}.
     */
    @JsonProperty("password")
    @Schema(description = "Пароль", example = "Pass1234", minLength = 8, maxLength = 16)
    private String password;

    /**
     * Имя пользователя.
     */
    @JsonProperty("firstName")
    @Schema(description = "Имя", example = "Иван", minLength = 2, maxLength = 16)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @JsonProperty("lastName")
    @Schema(description = "Фамилия", example = "Иванов", minLength = 2, maxLength = 16)
    private String lastName;

    /**
     * Телефон пользователя.
     * Валидируется регулярным выражением.
     */
    @JsonProperty("phone")
    @Schema(
            description = "Телефон",
            example = "+7 (999) 123-45-67",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}"
    )
    private String phone;

    /**
     * Роль пользователя.
     * Может быть USER или ADMIN, но обычно указывается USER.
     * Если значение не указано или некорректно — сервис выставит USER.
     */
    @JsonProperty("role")
    @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN"})
    private String role;
}

