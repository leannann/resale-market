package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("image")
    private String avatarUrl;
}
