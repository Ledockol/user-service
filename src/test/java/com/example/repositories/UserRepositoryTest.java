package com.example.repositories;

import com.example.models.User;
import com.example.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);

        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findById() {
        Optional<User> foundUser = userRepository.findById(1L);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    void findByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com"); // Исправлено
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(1L);
    }

    @Test
    void saveUser() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser.setAge(30);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("New User");
    }

    @Test
    void deleteUser() {
        userRepository.deleteById(1L);
        Optional<User> foundUser = userRepository.findById(1L);
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void findAll() {
        User secondUser = new User();
        secondUser.setName("Second User");
        secondUser.setEmail("second@example.com");
        secondUser.setAge(28);
        entityManager.persist(secondUser);
        entityManager.flush();

        Iterable<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
    }
}
