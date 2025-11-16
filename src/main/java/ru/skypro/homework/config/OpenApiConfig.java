package ru.skypro.homework.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI/Swagger для проекта.
 *
 * <p>Определяет:
 * <ul>
 *     <li>общую информацию о REST API (название, версия, описание);</li>
 *     <li>настройки серверов (адреса, с которых доступен API);</li>
 *     <li>группы тегов для Swagger UI для удобной навигации;</li>
 * </ul>
 *
 * <p>Используется библиотекой <b>springdoc-openapi</b> для генерации Swagger UI
 * по адресу:
 * <pre>http://localhost:8080/swagger-ui/index.html</pre>
 */
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
                @Tag(name = "Регистрация", description = "Метод для регистрации пользователей"),
                @Tag(name = "Авторизация", description = "Метод для входа пользователей в систему"),
                @Tag(name = "Комментарии", description = "Методы для работы с комментариями к объявлениям"),
                @Tag(name = "Объявления", description = "CRUD-операции и работа с изображениями объявлений"),
                @Tag(name = "Пользователи", description = "Операции с профилем авторизованного пользователя")
        }
)
public class OpenApiConfig { }
