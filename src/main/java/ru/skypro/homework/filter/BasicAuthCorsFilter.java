package ru.skypro.homework.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, добавляющий CORS-заголовки для поддержки авторизации через Basic Auth.
 * <p>
 * Наследуется от {@link OncePerRequestFilter}, что гарантирует единичное выполнение
 * фильтра для каждого запроса.
 * <p>
 * Основная задача фильтра — добавить заголовок
 * <code>Access-Control-Allow-Credentials: true</code>,
 * чтобы браузер разрешал передачу cookie и авторизационных данных
 * при cross-origin запросах.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Добавляет необходимые CORS-заголовки и передаёт запрос дальше по цепочке фильтров.
     *
     * @param httpServletRequest  входящий HTTP-запрос
     * @param httpServletResponse HTTP-ответ
     * @param filterChain         цепочка фильтров
     * @throws ServletException если произошла ошибка в цепочке фильтров
     * @throws IOException      если возникла ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

