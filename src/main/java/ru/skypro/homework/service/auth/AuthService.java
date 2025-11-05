package ru.skypro.homework.service.auth;
import ru.skypro.homework.dto.RegisterDto;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterDto register);
}
