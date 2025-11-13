    package ru.skypro.homework.dto;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import io.swagger.v3.oas.annotations.media.Schema;
    import lombok.Data;

    @Data
    @Schema(description = "Данные для регистрации пользователя")
    public class RegisterDto {
        @JsonProperty("username")
        @Schema(description = "Логин", example = "user1", minLength = 4, maxLength = 32)
        private String username;

        @JsonProperty("password")
        @Schema(description = "Пароль", example = "Pass1234", minLength = 8, maxLength = 16)
        private String password;

        @JsonProperty("firstName")
        @Schema(description = "Имя", example = "Иван", minLength = 2, maxLength = 16)
        private String firstName;

        @JsonProperty("lastName")
        @Schema(description = "Фамилия", example = "Иванов", minLength = 2, maxLength = 16)
        private String lastName;

        @JsonProperty("phone")
        @Schema(description = "Телефон", example = "+7 (999) 123-45-67",
                pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
        private String phone;

        @JsonProperty("role")
        @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN"})
        private String role;
    }
