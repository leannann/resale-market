package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ru.skypro.homework.repository.UserRepository;

import java.util.Arrays;

/**
 * Конфигурация безопасности приложения.
 * <p>
 * Настраивает:
 * <ul>
 *     <li>аутентификацию пользователей с использованием базы данных;</li>
 *     <li>настройки CORS для фронтенда;</li>
 *     <li>правила доступа к REST-эндпоинтам;</li>
 *     <li>HTTP Basic Authentication;</li>
 *     <li>поддержку аннотаций {@code @PreAuthorize} и {@code @PostAuthorize}.</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * Список путей, доступных без авторизации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/", "/error",
            "/login", "/register",
            "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    /**
     * Конфигурация источника данных для аутентификации пользователей.
     * <p>
     * Обрабатывает загрузку пользователя по email (username) через {@link UserRepository}.
     *
     * @param userRepository репозиторий пользователей
     * @return сервис загрузки данных пользователя
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Конфигурация кодировщика паролей.
     * <p>
     * Используется BCrypt — индустриальный стандарт для безопасного хеширования паролей.
     *
     * @return объект {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Конфигурация CORS для взаимодействия с фронтендом.
     * <p>
     * Разрешены локальные домены React-приложения.
     *
     * @return объект {@link CorsConfigurationSource} с разрешёнными доменами и методами
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("Разрешённый путь: http://localhost:3000");

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:3001"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Основная конфигурация цепочки фильтров Spring Security.
     * <p>
     * Настройки включают:
     * <ul>
     *     <li>разрешение CORS;</li>
     *     <li>отключение CSRF для REST-архитектуры;</li>
     *     <li>открытый доступ кSwagger и отдельным служебным маршрутам;</li>
     *     <li>требование авторизации для всех остальных API;</li>
     *     <li>включение HTTP Basic Authentication.</li>
     * </ul>
     *
     * @param http объект конфигурации Spring Security
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникла ошибка при настройке
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf()
                .disable()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/register"),
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/v3/api-docs"),
                                new AntPathRequestMatcher("/v3/api-docs/**"),
                                new AntPathRequestMatcher("/swagger-ui.html"),
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/swagger-resources/**"),
                                new AntPathRequestMatcher("/webjars/**"),
                                new AntPathRequestMatcher("/debug/**")
                        ).permitAll()
                        .mvcMatchers("/ads/**", "/users/**")
                        .authenticated()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic();

        return http.build();
    }
}

