package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO, представляющий коллекцию объявлений.
 * <p>
 * Используется для передачи списка объявлений вместе
 * с общим количеством найденных элементов.
 * Применяется в API для отображения списка объявлений на фронтенде.
 */
@Data
@Schema(description = "Список объявлений")
public class AdsDto {

    /**
     * Общее количество объявлений.
     * <p>
     * Может использоваться в пагинации или для отображения количества результатов.
     */
    @JsonProperty("count")
    @Schema(description = "Общее количество объявлений", example = "0")
    private Integer count;

    /**
     * Список объявлений.
     * <p>
     * Каждый элемент представлен объектом {@link AdDto}.
     */
    @JsonProperty("results")
    @Schema(description = "Список объявлений")
    private List<AdDto> results;
}

