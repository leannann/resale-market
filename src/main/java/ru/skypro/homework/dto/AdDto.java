package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, представляющий краткую информацию об объявлении.
 * <p>
 * Используется в списках объявлений, например на главной странице или в личном кабинете.
 * Включает основные данные: идентификатор, автора, заголовок, цену и ссылку на изображение.
 */
@Data
@Schema(description = "Краткая карточка объявления")
public class AdDto {

    /**
     * Идентификатор автора объявления.
     */
    @JsonProperty("author")
    @Schema(description = "ID автора объявления", example = "12")
    private Integer author;

    /**
     * Ссылка на изображение, прикреплённое к объявлению.
     * <p>
     * Как правило, это локальный URL, формируемый сервисом сохранения изображений.
     */
    @JsonProperty("image")
    @Schema(description = "Ссылка на картинку объявления", example = "/images/ads/12.jpg")
    private String image;

    /**
     * Уникальный идентификатор объявления.
     */
    @JsonProperty("pk")
    @Schema(description = "ID объявления", example = "101")
    private Integer pk;

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
    @Schema(description = "Заголовок объявления", example = "Продам велосипед", minLength = 4, maxLength = 32)
    private String title;
}

