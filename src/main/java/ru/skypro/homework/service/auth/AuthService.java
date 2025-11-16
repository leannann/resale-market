package ru.skypro.homework.service.auth;

import ru.skypro.homework.dto.RegisterDto;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * <p>
 * Определяет операции для проверки учетных данных (логина и пароля),
 * а также создания новой учетной записи пользователя.
 */
public interface AuthService {

    /**
     * Проверяет корректность логина и пароля пользователя.
     *
     * @param userName логин или email пользователя
     * @param password пароль пользователя
     * @return {@code true}, если учетные данные корректны,
     *         {@code false} — если аутентификация не удалась
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register объект с данными для регистрации
     * @return {@code true}, если пользователь успешно зарегистрирован,
     *         {@code false} — если регистрация невозможна
     */
    boolean register(RegisterDto register);
}

