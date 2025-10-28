package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdDto {
    @JsonProperty("author")
    private Integer authorId;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("pk")
    private Integer id;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("title")
    private String title;
}