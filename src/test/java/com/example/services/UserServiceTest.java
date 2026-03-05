package com.example.services;

import com.example.dto.UserDto;
import com.example.models.User;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setAge(25);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);
        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getName()).isEqualTo("Test User");
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Optional<UserDto> foundUser = userService.getUserById(1L);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    void updateUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto updatedUser = userService.updateUser(1L, userDto);
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("Test User");
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> allUsers = userService.getAllUsers();
        assertThat(allUsers).hasSize(1);
    }

    @Test
    void findUserByEmail() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        Optional<UserDto> foundUser = userService.findUserByEmail("test@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }
}
