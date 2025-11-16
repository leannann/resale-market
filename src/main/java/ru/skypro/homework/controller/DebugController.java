package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Вспомогательный REST-контроллер для отладки аутентификации и ролей пользователей.
 * <p>
 * Не предназначен для использования в продакшене. Позволяет:
 * <ul>
 *     <li>просмотреть информацию о текущем пользователе и его токене;</li>
 *     <li>перезагрузить Authentication из базы данных;</li>
 *     <li>проверить наличие ролей USER/ADMIN и права на создание объявлений.</li>
 * </ul>
 */
@RestController
@RequestMapping("/debug")
@Tag(
        name = "Отладка",
        description = "Вспомогательные методы для проверки аутентификации и ролей пользователей"
)
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Возвращает информацию о текущем Authentication.
     *
     * @param authentication объект аутентификации, предоставляемый Spring Security
     * @return JSON-объект с признаком аутентификации, именем пользователя и его правами
     */
    @Operation(
            summary = "Информация о текущем токене",
            description = "Возвращает сведения о текущем пользователе и его ролях на основе Authentication"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация успешно получена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))
            )
    })
    @GetMapping("/token-info")
    public Map<String, Object> getTokenInfo(Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        info.put("authenticated", authentication != null);
        if (authentication != null) {
            info.put("username", authentication.getName());
            info.put("authorities", authentication.getAuthorities());
            info.put("details", authentication.getDetails());
        }
        return info;
    }

    /**
     * Перезагружает объект Authentication из базы данных.
     * <p>
     * Полезно, когда у пользователя изменились права (роль), но текущая сессия ещё не была обновлена.
     *
     * @param request HTTP-запрос (непосредственно не используется, но может быть полезен при отладке)
     * @return JSON-объект с сообщением и обновлёнными правами пользователя
     */
    @Operation(
            summary = "Перезагрузка Authentication",
            description = "Перечитывает данные пользователя из базы и обновляет Authentication в SecurityContext"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication обновлён (если пользователь был аутентифицирован)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))
            )
    })
    @PostMapping("/reload-auth")
    public Map<String, Object> reloadAuthentication(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null) {
            String username = currentAuth.getName();

            userRepository.findByEmail(username).ifPresent(user -> {
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build();

                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            });

            result.put("message", "Authentication reloaded");
            result.put("username", username);
            result.put("newAuthorities",
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        } else {
            result.put("message", "No authentication found in context");
        }

        return result;
    }

    /**
     * Проверяет наличие у текущего пользователя ролей USER и ADMIN
     * и вычисляет, может ли он создавать объявления.
     *
     * @param authentication текущий объект аутентификации
     * @return JSON-объект с флагами hasUserRole, hasAdminRole и canCreateAd
     */
    @Operation(
            summary = "Проверка прав на работу с объявлениями",
            description = "Показывает, есть ли у текущего пользователя роли USER/ADMIN и можно ли ему создавать объявления"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Результат проверки прав успешно возвращён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))
            )
    })
    @GetMapping("/check-ad-permission")
    public Map<String, Object> checkAdPermission(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        if (authentication != null) {
            boolean hasUserRole = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
            boolean hasAdminRole = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            result.put("hasUserRole", hasUserRole);
            result.put("hasAdminRole", hasAdminRole);
            result.put("canCreateAd", hasUserRole);
            result.put("username", authentication.getName());
        } else {
            result.put("hasUserRole", false);
            result.put("hasAdminRole", false);
            result.put("canCreateAd", false);
        }

        return result;
    }
}

