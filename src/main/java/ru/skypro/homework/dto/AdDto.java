package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Краткая карточка объявления")
public class AdDto {
    @JsonProperty("author")
    @Schema(description = "ID автора объявления", example = "12")
    private Integer author;

    @JsonProperty("image")
    @Schema(description = "Ссылка на картинку объявления", example = "/images/ads/12.jpg")
    private String image;

    @JsonProperty("pk")
    @Schema(description = "ID объявления", example = "101")
    private Integer pk;

    @JsonProperty("price")
    @Schema(description = "Цена объявления", example = "15000", minimum = "0", maximum = "10000000")
    private Integer price;

    @JsonProperty("title")
    @Schema(description = "Заголовок объявления", example = "Продам велосипед", minLength = 4, maxLength = 32)
    private String title;
}
