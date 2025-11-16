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

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;

    public AuthServiceImpl(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository, RegisterMapper registerMapper) {
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.registerMapper = registerMapper;
    }

    @Override
    public boolean login(String email, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            return encoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        // Проверяем по username (который станет email)
        String email = registerDto.getUsername();

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Попытка зарегистрировать уже существующего пользователя с email: {}", email);
            return false;
        }

        // Проверка обязательных полей
        if (registerDto.getFirstName() == null || registerDto.getLastName() == null || registerDto.getPhone() == null) {
            log.error("Обязательные поля не заполнены для username: {}", email);
            return false; // или бросьте исключение
        }

        try {
            Role role = Role.USER;
            if (registerDto.getRole() != null) {
                try {
                    role = Role.valueOf(registerDto.getRole().toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("Некорректная роль: {}, установлена роль USER по умолчанию", registerDto.getRole());
                }
            }

            User user = registerMapper.registerDtoToUser(registerDto);
            // Установка роли и зашифрованного пароля
            user.setRole(role);
            user.setPassword(encoder.encode(registerDto.getPassword()));

            userRepository.save(user);
            log.info("Пользователь успешно зарегистрирован с email: {} с ролью: {}", email, role);
            return true;

        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", email, e);
            return false;
        }
    }
}
