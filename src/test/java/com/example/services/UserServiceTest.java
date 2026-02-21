package com.example.services;

import com.example.dao.UserDao;
import com.example.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserDao mockDao;

    @InjectMocks
    private UserService service = new UserServiceImpl(mockDao);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User("Test User", "test@test.com", 25);
        when(mockDao.save(any(User.class)))
                .thenReturn(user);
        User createdUser = service.createUser(user);
        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getAge(), createdUser.getAge());
        verify(mockDao, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User(1L, "Test User", "test@test.com", 25);
        when(mockDao.update(any(User.class)))
                .thenReturn(user);
        User updatedUser = service.updateUser(user);
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        verify(mockDao, times(1)).update(user);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        when(mockDao.findById(userId))
                .thenReturn(new User(userId, "Test", "test@test.com", 25));
        service.deleteUser(userId);
        verify(mockDao, times(1)).findById(userId);
        verify(mockDao, times(1)).delete(any(User.class));
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User expectedUser = new User(userId, "Test", "test@test.com", 25);
        when(mockDao.findById(userId))
                .thenReturn(expectedUser);
        User foundUser = service.getUserById(userId);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void testFindByEmail() {
        String email = "test@test.com";
        User expectedUser = new User(1L, "Test", email, 25);
        when(mockDao.findByEmail(email))
                .thenReturn(expectedUser);
        User foundUser = service.findByEmail(email);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    void testFindUsersByName() {
        String name = "Test";
        List<User> expectedUsers = List.of(
                new User(1L, name, "test1@test.com", 25),
                new User(2L, name, "test2@test.com", 30)
        );
        when(mockDao.findByName(name))
                .thenReturn(expectedUsers);
        List<User> foundUsers = service.findUsersByName(name);
        assertEquals(expectedUsers, foundUsers);
    }

    @Test
    void testFindUsersByAge() {
        int age = 25;
        List<User> expectedUsers = List.of(
                new User(1L, "Test1", "test1@test.com", age),
                new User(2L, "Test2", "test2@test.com", age)
        );
        when(mockDao.findByAge(age))
                .thenReturn(expectedUsers);
        List<User> foundUsers = service.findUsersByAge(age);
        assertEquals(expectedUsers, foundUsers);
    }

    @Test
    void testGetAllUsers() {
        List<User> expectedUsers = List.of(
                new User(1L, "User1", "user1@test.com", 25),
                new User(2L, "User2", "user2@test.com", 30),
                new User(3L, "User3", "user3@test.com", 22)
        );

        when(mockDao.findAll())
                .thenReturn(expectedUsers);

        List<User> allUsers = service.getAllUsers();

        assertEquals(expectedUsers, allUsers);
        verify(mockDao, times(1)).findAll();
    }
}