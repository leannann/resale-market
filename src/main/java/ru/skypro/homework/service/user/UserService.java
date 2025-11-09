package ru.skypro.homework.service.user;

import ru.skypro.homework.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(Integer id);

    UserDto createUser(UserDto userDto);

    void deleteUser(Integer id);
}
