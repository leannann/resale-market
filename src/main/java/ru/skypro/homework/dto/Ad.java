package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ad {
    private Integer author; // id автора
    private String image;   // ссылка на картинку
    private Integer pk;     // id объявления
    private Integer price;
    private String title;
}
