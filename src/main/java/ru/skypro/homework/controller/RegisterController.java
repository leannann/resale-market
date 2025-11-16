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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.auth.AuthService;

/**
 * REST-контроллер, отвечающий за регистрацию новых пользователей.
 * <p>
 * Содержит единственный эндпоинт, позволяющий создать нового пользователя в системе.
 * В случае успешной регистрации возвращает HTTP 201,
 * при ошибочных данных — HTTP 400.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(
        name = "Регистрация",
        description = "Метод для регистрации новых пользователей"
)
public class RegisterController {

    private final AuthService authService;

    /**
     * Регистрирует нового пользователя на основе данных, переданных в {@link RegisterDto}.
     *
     * @param register DTO с регистрационными данными пользователя
     * @return статус 201 — если регистрация успешна, 400 — если данные некорректны
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя. Возвращает 201 при успешной регистрации и 400 при ошибке.",
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
    public ResponseEntity<Void> register(
            @org.springframework.web.bind.annotation.RequestBody RegisterDto register
    ) {
        log.info("Попытка регистрации пользователя: username={}", register.getUsername());

        boolean success = authService.register(register);

        if (success) {
            log.info("Регистрация успешна: username={}", register.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Ошибка регистрации: username={}", register.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
