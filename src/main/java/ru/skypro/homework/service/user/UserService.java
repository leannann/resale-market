package ru.skypro.homework.service.user;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

/**
 * Сервис для работы с профилем пользователя.
 * <p>
 * Содержит операции по получению данных текущего пользователя,
 * обновлению профиля, смене пароля и загрузке аватара.
 */
public interface UserService {

    /**
     * Возвращает данные текущего пользователя по его email.
     *
     * @param email email пользователя
     * @return DTO с данными пользователя
     */
    UserDto getCurrentUser(String email);

    /**
     * Обновляет пароль пользователя.
     *
     * @param email       email пользователя
     * @param newPassword объект с текущим и новым паролем
     */
    void updatePassword(String email, NewPasswordDto newPassword);

    /**
     * Обновляет информацию профиля пользователя.
     *
     * @param email      email пользователя
     * @param updateUser DTO с обновлёнными данными пользователя
     * @return обновлённые данные пользователя
     */
    UpdateUserDto updateUser(String email, UpdateUserDto updateUser);

    /**
     * Обновляет аватар пользователя.
     *
     * @param email email пользователя
     * @param image файл изображения для нового аватара
     */
    void updateUserImage(String email, MultipartFile image);
}

