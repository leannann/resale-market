package ru.skypro.homework.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Данные для создания/обновления объявления")
public class CreateOrUpdateAdDto {
    @JsonProperty("title")
    @Schema(description = "Заголовок объявления", example = "Горный велосипед", minLength = 4, maxLength = 32)
    private String title;

    @JsonProperty("price")
    @Schema(description = "Цена объявления", example = "12000", minimum = "0", maximum = "10000000")
    private Integer price;

    @JsonProperty("description")
    @Schema(description = "Описание объявления", example = "Практически не использовался", minLength = 8, maxLength = 64)
    private String description;
}
