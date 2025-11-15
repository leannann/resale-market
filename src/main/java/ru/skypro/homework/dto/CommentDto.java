package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Комментарий к объявлению")
public class CommentDto {
    @JsonProperty("author")
    @Schema(description = "ID автора комментария", example = "12")
    private Integer author;

    @JsonProperty("authorImage")
    @Schema(description = "Ссылка на аватар автора комментария", example = "/images/avatars/12.jpg")
    private String authorImage;

    @JsonProperty("authorFirstName")
    @Schema(description = "Имя автора комментария", example = "Иван")
    private String authorFirstName;

    @JsonProperty("createdAt")
    @Schema(description = "Дата/время создания (epoch millis с 01.01.1970 00:00:00 UTC)", example = "1698672000000")
    private Long createdAt;

    @JsonProperty("pk")
    @Schema(description = "ID комментария", example = "501")
    private Integer pk;

    @JsonProperty("text")
    @Schema(description = "Текст комментария", example = "Отличное состояние!", minLength = 8, maxLength = 64)
    private String text;
}
