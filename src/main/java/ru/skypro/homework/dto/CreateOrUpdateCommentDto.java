package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrUpdateCommentDto {
    @JsonProperty("text")
    private String text;
}