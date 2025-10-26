package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Comment {
    private Integer author;         // id автора
    private String authorImage;     // ссылка на аватар
    private String authorFirstName; // имя автора
    private Long createdAt;         // epoch millis
    private Integer pk;             // id комментария
    private String text;
}