package com.example.services;

import com.example.dto.UserDto;
import com.example.exceptions.ResourceNotFoundException;
import com.example.models.User;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto testUserDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Подготовка тестовых данных
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("Test User");
        testUserDto.setEmail("test@example.com");
        testUserDto.setAge(25);
        testUserDto.setPhone("1234567890");
        testUserDto.setRole("USER");
        testUserDto.setPassword("password");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAge(25);
        testUser.setPhone("1234567890");
        testUser.setRole("USER");
        testUser.setPassword("encodedPassword");
        testUser.setRegistrationDate(LocalDate.now());
    }

    @Test
    void createUser_shouldCreateUser() {
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(testUser);

        UserDto createdUser = userService.createUser(testUserDto);

        assertEquals(testUserDto.getName(), createdUser.getName());
        assertEquals(testUserDto.getEmail(), createdUser.getEmail());
        assertEquals(testUserDto.getAge(), createdUser.getAge());
        assertEquals(testUserDto.getPhone(), createdUser.getPhone());
        assertEquals(testUserDto.getRole(), createdUser.getRole());
    }

    @Test
    void getUserById_shouldReturnUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

        UserDto user = userService.getUserById(testUser.getId()).orElseThrow();

        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    void getUserById_shouldThrowException() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void updateUser_shouldUpdateUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));
        given(userRepository.save(any(User.class))).willReturn(testUser);

        UserDto updatedUser = userService.updateUser(testUser.getId(), testUserDto).orElseThrow();

        assertEquals(testUserDto.getName(), updatedUser.getName());
        assertEquals(testUserDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

        userService.deleteUser(testUser.getId());

        verify(userRepository).deleteById(testUser.getId());
    }

    @Test
    void deleteUser_shouldThrowException() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
    }

    @Test
    void findUserByEmail_shouldFindUser() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(testUser));

        UserDto foundUser = userService.findUserByEmail(testUser.getEmail())
                .orElseThrow();

        assertEquals(testUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void findUserByEmail_shouldReturnEmpty() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        Optional<UserDto> foundUser = userService.findUserByEmail("nonexistent@email.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void getAllUsers_shouldReturnList() {
        given(userRepository.findAll()).willReturn(List.of(testUser));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(testUser.getName(), users.get(0).getName());
    }

    @Test
    void convertDtoToEntity_shouldConvertCorrectly() {
        User convertedUser = userService.convertDtoToEntity(testUserDto);

        assertEquals(testUserDto.getName(), convertedUser.getName());
        assertEquals(testUserDto.getEmail(), convertedUser.getEmail());
        assertEquals(testUserDto.getAge(), convertedUser.getAge());
        assertEquals(testUserDto.getPhone(), convertedUser.getPhone());
        assertEquals(testUserDto.getRole(), convertedUser.getRole());
    }

    @Test
    void convertEntityToDto_shouldConvertCorrectly() {
        UserDto convertedDto = userService.convertEntityToDto(testUser);

        assertEquals(testUser.getId(), convertedDto.getId());
        assertEquals(testUser.getName(), convertedDto.getName());
        assertEquals(testUser.getEmail(), convertedDto.getEmail());
        assertEquals(testUser.getAge(), convertedDto.getAge());
        assertEquals(testUser.getPhone(), convertedDto.getPhone());
        assertEquals(testUser.getRole(), convertedDto.getRole());
    }
}
