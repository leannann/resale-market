package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.user.UserService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UsersController {
    private final UserService userService;

    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPasswordDto newPassword, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Попытка обновления пароля неаутентифицированным пользователем");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        log.info("Запрос на обновление пароля от пользователя: {}", username);

        userService.updatePassword(authentication.getName(), newPassword);

        log.info("Пароль успешно обновлен для пользователя: {}", username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Попытка получения информации о пользователе неаутентифицированным пользователем");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        log.info("Запрос информации о текущем пользователе: {}", username);

        UserDto user = userService.getCurrentUser(authentication.getName());

        log.info("Информация о пользователе получена: {} (ID: {})", username, user.getId());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto updateUser, Authentication authentication) {
        String username = authentication.getName();
        log.info("Запрос на обновление профиля от пользователя: {}", username);
        log.debug("Данные для обновления: имя='{}', фамилия='{}', телефон='{}'", updateUser.getFirstName(), updateUser.getLastName(), updateUser.getPhone());

        UpdateUserDto updatedUser = userService.updateUser(authentication.getName(), updateUser);

        log.info("Профиль пользователя обновлен: {}", username);
        log.debug("Обновленные данные: имя='{}', фамилия='{}', телефон='{}'", updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getPhone());
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Обновление аватара пользователя")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image, Authentication authentication) {
        String username = authentication.getName();
        log.info("Запрос на обновление аватара от пользователя: {}", username);
        log.debug("Файл изображения: имя='{}', размер={} байт, тип={}", image.getOriginalFilename(), image.getSize(), image.getContentType());

        userService.updateUserImage(authentication.getName(), image);

        log.info("Аватар пользователя обновлен: {}", username);
        return ResponseEntity.ok().build();
    }
}

