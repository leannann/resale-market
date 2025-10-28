package ru.skypro.homework.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Resale Market API",
                version = "1.0.0",
                description = "Документация REST API для сервиса перепродажи товаров"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Dev")
        },
        tags = {
                @Tag(name = "Авторизация и регистрация", description = "Методы для входа и регистрации пользователей"),
                @Tag(name = "Комментарии", description = "Методы для работы с комментариями к объявлениям"),
                @Tag(name = "Объявления", description = "CRUD-операции и работа с изображениями объявлений"),
                // ⬇️ ДОБАВЛЯЕМ ЭТО
                @Tag(name = "Пользователи", description = "Операции с профилем авторизованного пользователя")
        }
)
public class OpenApiConfig { }