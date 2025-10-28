package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExtendedAdDto {
    @JsonProperty("pk")
    private Integer id;

    @JsonProperty("authorFirstName")
    private String authorFirstName;

    @JsonProperty("authorLastName")
    private String authorLastName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("email")
    private String authorEmail;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("phone")
    private String authorPhone;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("title")
    private String title;
}
