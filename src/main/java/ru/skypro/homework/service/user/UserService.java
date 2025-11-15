package ru.skypro.homework.service.user;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {
    UserDto getCurrentUser(String email);

    void updatePassword(String email, NewPasswordDto newPassword);

    UpdateUserDto updateUser(String email, UpdateUserDto updateUser);

    void updateUserImage(String email, MultipartFile image);
}
