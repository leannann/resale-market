package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

/**
 * Маппер для преобразования между сущностью пользователя {@link User}
 * и пользовательским DTO {@link UserDto}.
 * <p>
 * Использует MapStruct для автоматической генерации реализации.
 * Применяется в сервисах при возврате данных о пользователе.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует сущность {@link User} в объект {@link UserDto}.
     * <p>
     * Все основные поля пользователя копируются напрямую, включая роль.
     *
     * @param user сущность пользователя
     * @return объект {@link UserDto}
     */
    @Mapping(target = "role", source = "role")
    UserDto userToUserDto(User user);

    /**
     * Преобразует DTO пользователя в сущность {@link User}.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code password} — игнорируется для безопасности;</li>
     *     <li>{@code ads} — игнорируется, так как объявления не передаются в DTO;</li>
     *     <li>{@code comments} — игнорируется;</li>
     *     <li>{@code enabled} — игнорируется и устанавливается в сервисах;</li>
     * </ul>
     * Полезно при обновлении профиля пользователя.
     *
     * @param userDto пользовательский DTO
     * @return сущность {@link User}
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User userDtoToUser(UserDto userDto);
}

