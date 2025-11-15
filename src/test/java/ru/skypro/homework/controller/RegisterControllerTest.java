package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.auth.AuthService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@Import(TestSecurityConfig.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @WithMockUser
    void register_created() throws Exception {
        RegisterDto req = new RegisterDto();
        req.setUsername("user1@example.com");
        req.setPassword("Pass1234");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPhone("+79991234567");

        when(authService.register(req)).thenReturn(true);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void register_badRequest() throws Exception {
        RegisterDto req = new RegisterDto();
        req.setUsername("user1@example.com");
        req.setPassword("Pass1234");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPhone("+79991234567");

        when(authService.register(req)).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void register_withMissingFields_badRequest() throws Exception {
        RegisterDto req = new RegisterDto();
        req.setUsername("user1@example.com");
        req.setPassword("Pass1234");
        // Отсутствуют firstName, lastName, phone

        when(authService.register(req)).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
