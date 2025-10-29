package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список объявлений")
public class AdsDto {
    @JsonProperty("count")
    @Schema(description = "Общее количество объявлений", example = "0")
    private Integer count;

    @JsonProperty("results")
    @Schema(description = "Список объявлений")
    private List<AdDto> results;
}
