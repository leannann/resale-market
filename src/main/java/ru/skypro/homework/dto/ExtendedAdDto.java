package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Расширенное описание объявления")
public class ExtendedAdDto {
    @JsonProperty("pk")
    @Schema(description = "ID объявления", example = "101")
    private Integer id;

    @JsonProperty("authorFirstName")
    @Schema(description = "Имя автора объявления", example = "Иван")
    private String authorFirstName;

    @JsonProperty("authorLastName")
    @Schema(description = "Фамилия автора объявления", example = "Иванов")
    private String authorLastName;

    @JsonProperty("description")
    @Schema(description = "Описание объявления", example = "Почти новый, 2 сезона", minLength = 8, maxLength = 64)
    private String description;

    @JsonProperty("email")
    @Schema(description = "Логин (email) автора объявления", example = "user1@example.com")
    private String authorEmail;

    @JsonProperty("image")
    @Schema(description = "Ссылка на картинку объявления", example = "/images/ads/101.jpg")
    private String imageUrl;

    @JsonProperty("phone")
    @Schema(description = "Телефон автора объявления", example = "+7 (999) 123-45-67")
    private String authorPhone;

    @JsonProperty("price")
    @Schema(description = "Цена объявления", example = "15000", minimum = "0", maximum = "10000000")
    private Integer price;

    @JsonProperty("title")
    @Schema(description = "Заголовок объявления", example = "Велосипед Trek FX", minLength = 4, maxLength = 32)
    private String title;
}
