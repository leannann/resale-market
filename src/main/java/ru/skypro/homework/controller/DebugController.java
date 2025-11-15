package ru.skypro.homework.controller;

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

@RestController
@RequestMapping("/debug")
public class DebugController {
    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/reload-auth")
    public Map<String, Object> reloadAuthentication(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // Получаем текущего пользователя
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null) {
            String username = currentAuth.getName();

            // Загружаем свежие данные из БД
            userRepository.findByEmail(username).ifPresent(user -> {
                // Создаем новый Authentication с обновленными authorities
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
            result.put("newAuthorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }

        return result;
    }


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
            result.put("canCreateAd", hasUserRole); // Требуемая роль
            result.put("username", authentication.getName());
        } else {
            result.put("hasUserRole", false);
            result.put("hasAdminRole", false);
            result.put("canCreateAd", false);
        }

        return result;
    }
}
