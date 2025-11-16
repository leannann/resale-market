package ru.skypro.homework.service.auth.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mappers.RegisterMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.auth.AuthService;

/**
 * Реализация сервиса {@link AuthService} для аутентификации и регистрации пользователей.
 * <p>
 * Обеспечивает проверку учетных данных пользователя при входе,
 * а также создание новой учетной записи с проверкой корректности данных
 * и уникальности email.
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;

    /**
     * Конструктор сервиса.
     *
     * @param userDetailsService сервис получения данных пользователя для аутентификации
     * @param passwordEncoder    компонент для шифрования паролей
     * @param userRepository     репозиторий пользователей
     * @param registerMapper     маппер DTO регистрации в сущность пользователя
     */
    public AuthServiceImpl(UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RegisterMapper registerMapper) {
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.registerMapper = registerMapper;
    }

    /**
     * Выполняет проверку логина и пароля пользователя.
     * <p>
     * Метод ищет пользователя в системе и сравнивает переданный пароль с
     * зашифрованным значением, хранящимся в базе.
     *
     * @param email    email пользователя (логин)
     * @param password пароль пользователя
     * @return {@code true}, если учетные данные корректны; {@code false} — иначе
     */
    @Override
    public boolean login(String email, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            return encoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    /**
     * Регистрирует нового пользователя.
     * <p>
     * Проверяет уникальность email, корректность обязательных данных
     * и устанавливает роль и зашифрованный пароль перед сохранением.
     *
     * @param registerDto данные для регистрации нового пользователя
     * @return {@code true}, если регистрация прошла успешно; {@code false} — при ошибке
     */
    @Override
    public boolean register(RegisterDto registerDto) {
        String email = registerDto.getUsername();

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Попытка зарегистрировать уже существующего пользователя: {}", email);
            return false;
        }

        if (registerDto.getFirstName() == null ||
                registerDto.getLastName() == null ||
                registerDto.getPhone() == null) {
            log.error("Обязательные поля не заполнены для пользователя: {}", email);
            return false;
        }

        try {
            Role role = Role.USER;
            if (registerDto.getRole() != null) {
                try {
                    role = Role.valueOf(registerDto.getRole().toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("Некорректная роль: {}. Установлена роль USER по умолчанию", registerDto.getRole());
                }
            }

            User user = registerMapper.registerDtoToUser(registerDto);
            user.setRole(role);
            user.setPassword(encoder.encode(registerDto.getPassword()));

            userRepository.save(user);
            log.info("Пользователь успешно зарегистрирован: {} с ролью: {}", email, role);

            return true;

        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", email, e);
            return false;
        }
    }
}

