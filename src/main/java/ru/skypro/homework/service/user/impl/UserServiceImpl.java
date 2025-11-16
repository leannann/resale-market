package ru.skypro.homework.service.user.impl;

import java.io.IOException;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.image.ImageService;
import ru.skypro.homework.service.user.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @Override
    public UserDto getCurrentUser(String email) {
        log.debug("Получение информации о пользователе: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        log.info("Информация о пользователе получена: {}", email);

        return userMapper.userToUserDto(user);
    }

    @Override
    public void updatePassword(String email, NewPasswordDto newPassword) {
        log.debug("Обновление пароля пользователя: {}", email);

        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        // Проверка текущего пароля
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            log.warn("Неверный текущий пароль для пользователя: {}", email);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Неверный текущий пароль");
        }

        // Проверка, что новый пароль отличается от старого
        if (passwordEncoder.matches(newPassword.getNewPassword(), user.getPassword())) {
            log.warn("Новый пароль совпадает со старым для пользователя: {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Новый пароль должен отличаться от старого");
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        log.info("Пароль успешно обновлен для пользователя: {}", email);
    }

    @Override
    public UpdateUserDto updateUser(String email, UpdateUserDto updateUser) {
        log.debug("Обновление информации пользователя: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        if (updateUser.getFirstName() != null) {
            user.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            user.setLastName(updateUser.getLastName());
        }
        if (updateUser.getPhone() != null) {
            user.setPhone(updateUser.getPhone());
        }

        User updatedUser = userRepository.save(user);
        log.info("Информация пользователя обновлена: {}", email);

        UpdateUserDto response = new UpdateUserDto();
        response.setFirstName(updatedUser.getFirstName());
        response.setLastName(updatedUser.getLastName());
        response.setPhone(updatedUser.getPhone());
        return response;
    }

    @Override
    public void updateUserImage(String email, MultipartFile image) {
        log.debug("Обновление аватара пользователя: {}", email);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn("Пользователь не найден: {}", email);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                    });

            String imagePath = imageService.saveImage(image, "avatars");
            user.setImage(imagePath);
            userRepository.save(user);
            log.info("Аватар пользователя обновлен: {}", email);
        } catch (IOException e) {
            log.error("Ошибка при сохранении изображения для пользователя {}: {}", email, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка при загрузке изображения");
        }
    }
}
