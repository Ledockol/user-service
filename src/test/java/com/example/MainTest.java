package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.List;

import com.example.models.User;
import com.example.services.UserService;

class MainTest {

    @Mock
    private UserService mockService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        originalOut = System.out;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testCreateUser() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try (Scanner scanner = new Scanner(System.in)) {
            User mockUser = new User("Test", "test@test.com", 25);
            when(mockService.createUser(any(User.class))).thenReturn(mockUser);

            ByteArrayInputStream inContent = new ByteArrayInputStream("Test\ntest@test.com\n25\n".getBytes());
            System.setIn(inContent);

            Main.createUser(scanner);

            verify(mockService, times(1)).createUser(userCaptor.capture());
            User createdUser = userCaptor.getValue();

            assertThat(createdUser.getName()).isEqualTo("Test");
            assertThat(createdUser.getEmail()).isEqualTo("test@test.com");
            assertThat(createdUser.getAge()).isEqualTo(25);

            String output = outContent.toString();
            assertThat(output).contains("Пользователь создан");
        }
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "Test", "test@test.com", 25);
        when(mockService.getUserById(1L)).thenReturn(user);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try (Scanner scanner = new Scanner(System.in)) {
            ByteArrayInputStream inContent = new ByteArrayInputStream("1\n".getBytes());
            System.setIn(inContent);

            Main.getUserById(scanner);

            verify(mockService, times(1)).getUserById(1L);
            String output = outContent.toString();
            assertThat(output).contains("Найден пользователь");
        }
    }

    @Test
    void testUpdateUser() {
        User user = new User(1L, "Test", "test@test.com", 25);
        when(mockService.getUserById(1L)).thenReturn(user);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try (Scanner scanner = new Scanner(System.in)) {
            ByteArrayInputStream inContent = new ByteArrayInputStream("1\nNewTest\nnew@test.com\n30\n".getBytes());
            System.setIn(inContent);

            Main.updateUser(scanner);

            verify(mockService, times(1)).updateUser(argThat(u ->
                    u.getId().equals(1L) &&
                            u.getName().equals("NewTest") &&
                            u.getEmail().equals("new@test.com") &&
                            u.getAge().equals(30)
            ));

            String output = outContent.toString();
            assertThat(output).contains("Пользователь успешно обновлен");
        }
    }

    @Test
    void testDeleteUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));


            ByteArrayInputStream inContent = new ByteArrayInputStream("1\n".getBytes());
            System.setIn(inContent);


            when(mockService.deleteUser(1L)).thenReturn(true);


            Main.deleteUser(scanner);


            verify(mockService, times(1)).deleteUser(1L);


            String output = outContent.toString();
            assertThat(output).contains("Пользователь с ID 1 успешно удален");
        }
    }

    @Test
    void testDeleteUserNotFound() {
        try (Scanner scanner = new Scanner(System.in)) {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));


            ByteArrayInputStream inContent = new ByteArrayInputStream("999\n".getBytes());
            System.setIn(inContent);


            when(mockService.deleteUser(999L)).thenReturn(false);


            Main.deleteUser(scanner);

            verify(mockService, times(1)).deleteUser(999L);


            String output = outContent.toString();
            assertThat(output).contains("Пользователь с ID 999 не найден");
        }
    }

    @Test
    void testDeleteUserInvalidInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));


            ByteArrayInputStream inContent = new ByteArrayInputStream("abc\n".getBytes());
            System.setIn(inContent);


            Main.deleteUser(scanner);

            verify(mockService, never()).deleteUser(anyLong());

            String output = outContent.toString();
            assertThat(output).contains("Неверный формат ID");
        }
    }
}