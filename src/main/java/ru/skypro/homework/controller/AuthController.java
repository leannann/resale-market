package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.service.auth.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "Метод для входа  пользователей в систему")
public class AuthController {

    private final AuthService authService;


    @Operation(
            summary = "Авторизация пользователя",
            description = "Проверяет логин и пароль пользователя. Возвращает 200 при успешной авторизации, 401 — при ошибке.",
            requestBody = @RequestBody(
                    description = "Данные для входа пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@org.springframework.web.bind.annotation.RequestBody LoginDto login) {
        log.info("Попытка входа в систему: username={}", login.getUsername());

        if (authService.login(login.getUsername(), login.getPassword())) {
            log.info("Вход в систему прошел успешно: username={}", login.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Ошибка входа в систему: username={} (invalid credentials)", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
