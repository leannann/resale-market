package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, представляющий расширенную информацию об объявлении.
 * <p>
 * Используется для отображения объявления на странице подробного просмотра.
 * Включает не только данные самого объявления, но и контактную информацию автора.
 */
@Data
@Schema(description = "Расширенное описание объявления")
public class ExtendedAdDto {

    /**
     * Уникальный идентификатор объявления.
     */
    @JsonProperty("pk")
    @Schema(description = "ID объявления", example = "101")
    private Integer pk;

    /**
     * Имя автора объявления.
     */
    @JsonProperty("authorFirstName")
    @Schema(description = "Имя автора объявления", example = "Иван")
    private String authorFirstName;

    /**
     * Фамилия автора объявления.
     */
    @JsonProperty("authorLastName")
    @Schema(description = "Фамилия автора объявления", example = "Иванов")
    private String authorLastName;

    /**
     * Подробное текстовое описание объявления.
     */
    @JsonProperty("description")
    @Schema(description = "Описание объявления", example = "Почти новый, 2 сезона", minLength = 8, maxLength = 64)
    private String description;

    /**
     * Email автора объявления.
     * Используется для связи и отображения профиля владельца.
     */
    @JsonProperty("email")
    @Schema(description = "Логин (email) автора объявления", example = "user1@example.com")
    private String email;

    /**
     * Ссылка на изображение объявления.
     * Как правило, локальный URL внутри backend-сервера.
     */
    @JsonProperty("image")
    @Schema(description = "Ссылка на картинку объявления", example = "/images/ads/101.jpg")
    private String image;

    /**
     * Телефон автора объявления.
     */
    @JsonProperty("phone")
    @Schema(description = "Телефон автора объявления", example = "+7 (999) 123-45-67")
    private String phone;

    /**
     * Цена объявления.
     */
    @JsonProperty("price")
    @Schema(description = "Цена объявления", example = "15000", minimum = "0", maximum = "10000000")
    private Integer price;

    /**
     * Заголовок объявления.
     */
    @JsonProperty("title")
    @Schema(description = "Заголовок объявления", example = "Велосипед Trek FX", minLength = 4, maxLength = 32)
    private String title;
}

