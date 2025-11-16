package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO, используемый для создания нового объявления
 * или обновления существующего.
 * <p>
 * Содержит основные данные объявления: заголовок, цену и описание.
 * Не включает изображение — оно передаётся отдельным Multipart-запросом.
 */
@Data
@Schema(description = "Данные для создания/обновления объявления")
public class CreateOrUpdateAdDto {

    /**
     * Заголовок объявления.
     * <p>
     * Должен быть информативным и соответствовать ограничениям по длине.
     */
    @JsonProperty("title")
    @Schema(description = "Заголовок объявления", example = "Горный велосипед", minLength = 4, maxLength = 32)
    private String title;

    /**
     * Стоимость товара или услуги.
     * <p>
     * Значение должно быть неотрицательным.
     */
    @JsonProperty("price")
    @Schema(description = "Цена объявления", example = "12000", minimum = "0", maximum = "10000000")
    private Integer price;

    /**
     * Подробное описание объявления.
     * <p>
     * Используется для отображения информации на странице объявления.
     */
    @JsonProperty("description")
    @Schema(description = "Описание объявления", example = "Практически не использовался", minLength = 8, maxLength = 64)
    private String description;
}

