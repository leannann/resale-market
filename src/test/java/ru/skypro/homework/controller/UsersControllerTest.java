package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.user.UserService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
@Import(TestSecurityConfig.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void setPassword_ok() throws Exception {
        NewPasswordDto dto = new NewPasswordDto();
        dto.setCurrentPassword("Pass1234");
        dto.setNewPassword("Pass5678");

        doNothing().when(userService).updatePassword(anyString(), any(NewPasswordDto.class));

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userService).updatePassword(anyString(), any(NewPasswordDto.class));
    }

    @Test
    @WithMockUser
    void getMe_ok() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("user@example.com");
        userDto.setFirstName("Иван");
        userDto.setLastName("Иванов");
        userDto.setPhone("+7 (900) 111-22-33");

        when(userService.getCurrentUser(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 111-22-33"));

        verify(userService).getCurrentUser(anyString());
    }

    @Test
    @WithMockUser
    void updateMe_ok() throws Exception {
        UpdateUserDto request = new UpdateUserDto();
        request.setFirstName("Иван");
        request.setLastName("Иванов");
        request.setPhone("+7 (900) 111-22-33");

        UpdateUserDto response = new UpdateUserDto();
        response.setFirstName("Иван");
        response.setLastName("Иванов");
        response.setPhone("+7 (900) 111-22-33");

        when(userService.updateUser(anyString(), any(UpdateUserDto.class))).thenReturn(response);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 111-22-33"));

        verify(userService).updateUser(anyString(), any(UpdateUserDto.class));
    }

    @Test
    @WithMockUser
    void updateAvatar_ok() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "avatar.jpg", "image/jpeg", new byte[]{1, 2, 3}
        );

        doNothing().when(userService).updateUserImage(anyString(), any(MultipartFile.class));

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        }))
                .andExpect(status().isOk());

        verify(userService).updateUserImage(anyString(), any(MultipartFile.class));
    }

    @Test
    void setPassword_unauthorized() throws Exception {
        NewPasswordDto dto = new NewPasswordDto();
        dto.setCurrentPassword("Pass1234");
        dto.setNewPassword("Pass5678");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMe_unauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }
}




