package com.example.controller;

import com.example.dto.UserDto;
import com.example.models.User;
import com.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setAge(25);
        userDto.setPhone("1234567890");
        userDto.setRole("USER");

        given(userService.createUser(userDto)).willReturn(userDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());

        verify(userService).createUser(userDto);
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Test User");

        given(userService.getUserById(userId)).willReturn(Optional.of(userDto));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @Test
    void getAllUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(List.of(
                new UserDto(1L, "User1", "user1@example.com"),
                new UserDto(2L, "User2", "user2@example.com")
        ));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());