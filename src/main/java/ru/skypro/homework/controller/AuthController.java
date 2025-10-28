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
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация и регистрация", description = "Методы для входа и регистрации пользователей")
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
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя. Возвращает 201 при успешной регистрации, 400 — при ошибке.",
            requestBody = @RequestBody(
                    description = "Данные для регистрации нового пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@org.springframework.web.bind.annotation.RequestBody RegisterDto register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
