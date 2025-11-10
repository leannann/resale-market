package ru.skypro.homework.service.auth.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.auth.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userManager;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!userManager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userManager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterDto register) {
        if (userManager.userExists(register.getUsername())) {
            return false;
        }
        userManager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());
        return true;
    }

}
