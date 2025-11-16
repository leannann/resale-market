package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;

/**
 * Маппер для преобразования DTO регистрации {@link RegisterDto}
 * в сущность пользователя {@link User}.
 * <p>
 * Используется для создания нового пользователя при регистрации.
 * Реализация маппера генерируется автоматически MapStruct.
 */
@Mapper(componentModel = "spring")
public interface RegisterMapper {

    /**
     * Преобразует данные регистрации в сущность пользователя.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code username → email} — логин маппится в email пользователя;</li>
     *     <li>основные поля ({@code firstName}, {@code lastName}, {@code phone}, {@code password}, {@code role})
     *         маппируются напрямую;</li>
     *     <li>{@code id}, {@code image}, {@code ads}, {@code comments} — игнорируются;</li>
     *     <li>{@code enabled} всегда устанавливается в {@code true};</li>
     *     <li>роль позже валидируется в сервисе регистрации.</li>
     * </ul>
     *
     * @param registerDto DTO с данными пользователя для регистрации
     * @return сущность пользователя {@link User}
     */
    @Mapping(target = "email", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    User registerDtoToUser(RegisterDto registerDto);
}

