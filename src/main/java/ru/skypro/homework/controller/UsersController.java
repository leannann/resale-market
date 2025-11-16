package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.user.UserService;

import javax.validation.Valid;

/**
 * REST-контроллер для работы с профилем авторизованного пользователя.
 * <p>
 * Предоставляет операции:
 * <ul>
 *     <li>смена пароля;</li>
 *     <li>получение информации о текущем пользователе;</li>
 *     <li>обновление профиля;</li>
 *     <li>обновление аватара.</li>
 * </ul>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(
        name = "Пользователи",
        description = "Операции с профилем текущего авторизованного пользователя"
)
public class UsersController {

    private final UserService userService;

    /**
     * Обновляет пароль текущего авторизованного пользователя.
     *
     * @param newPassword    DTO с текущим и новым паролем
     * @param authentication объект аутентификации текущего пользователя
     * @return HTTP 200 в случае успеха или 401, если пользователь не аутентифицирован
     */
    @Operation(
            summary = "Обновление пароля",
            description = "Позволяет авторизованному пользователю сменить свой пароль"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пароля"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(
            @Valid
            @RequestBody(
                    description = "Текущий и новый пароль пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewPasswordDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody NewPasswordDto newPassword,
            Authentication authentication) {

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

    /**
     * Возвращает информацию о текущем авторизованном пользователе.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @return данные пользователя в виде {@link UserDto} или 401, если пользователь не аутентифицирован
     */
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            description = "Возвращает профиль текущего авторизованного пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о пользователе успешно получена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
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

    /**
     * Обновляет профиль авторизованного пользователя (имя, фамилию и телефон).
     *
     * @param updateUser     DTO с новыми данными пользователя
     * @param authentication объект аутентификации текущего пользователя
     * @return обновлённые данные пользователя
     */
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            description = "Позволяет изменить имя, фамилию и телефон текущего пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Профиль пользователя успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные профиля"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserDto> updateUser(
            @RequestBody(
                    description = "Новые данные профиля пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody UpdateUserDto updateUser,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("Запрос на обновление профиля от пользователя: {}", username);
        log.debug("Данные для обновления: имя='{}', фамилия='{}', телефон='{}'",
                updateUser.getFirstName(), updateUser.getLastName(), updateUser.getPhone());

        UpdateUserDto updatedUser = userService.updateUser(authentication.getName(), updateUser);

        log.info("Профиль пользователя обновлен: {}", username);
        log.debug("Обновленные данные: имя='{}', фамилия='{}', телефон='{}'",
                updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getPhone());
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Обновляет аватар текущего авторизованного пользователя.
     *
     * @param image          файл изображения (аватар)
     * @param authentication объект аутентификации текущего пользователя
     * @return HTTP 200 в случае успешного обновления, либо 401 при отсутствии авторизации
     */
    @Operation(
            summary = "Обновление аватара пользователя",
            description = "Загружает и сохраняет новое изображение профиля текущего пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аватар успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Файл изображения отсутствует или некорректен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(
            @Parameter(
                    description = "Файл изображения (аватар пользователя)",
                    required = true
            )
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("Запрос на обновление аватара от пользователя: {}", username);
        log.debug("Файл изображения: имя='{}', размер={} байт, тип={}",
                image.getOriginalFilename(), image.getSize(), image.getContentType());

        userService.updateUserImage(authentication.getName(), image);

        log.info("Аватар пользователя обновлен: {}", username);
        return ResponseEntity.ok().build();
    }
}

