package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;         // USER / ADMIN
    private String image;      // ссылка на аватар
}
