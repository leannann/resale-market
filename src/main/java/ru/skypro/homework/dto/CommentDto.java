package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDto {
    @JsonProperty("author")
    private Integer authorId;

    @JsonProperty("authorImage")
    private String authorImageUrl;

    @JsonProperty("authorFirstName")
    private String authorFirstName;

    @JsonProperty("createdAt")
    private Long createdAtEpochMillis;

    @JsonProperty("pk")
    private Integer id;

    @JsonProperty("text")
    private String text;
}