package ru.skypro.homework.service.user.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collections;

/**
 * Реализация {@link UserDetailsService} для интеграции пользовательских данных
 * приложения со Spring Security.
 * <p>
 * Сервис обеспечивает загрузку пользователя по email и преобразование
 * сущности {@link ru.skypro.homework.entity.User} в объект {@link UserDetails},
 * необходимый для механизма аутентификации Spring Security.
 */
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     *
     * @param userRepository репозиторий пользователей
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по email и преобразует его в {@link UserDetails}.
     *
     * @param email email пользователя
     * @return объект UserDetails, содержащий данные пользователя для Spring Security
     * @throws UsernameNotFoundException если пользователь с указанным email не найден
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }
}

