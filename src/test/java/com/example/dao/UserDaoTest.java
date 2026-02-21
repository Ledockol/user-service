package com.example.dao;

import com.example.util.HibernateSessionFactoryUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hibernate.SessionFactory;
import com.example.dao.UserDaoImpl;
import com.example.models.User;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest {

    @Container
    private static final PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:13")
            .withInitScript("db/init-schema.sql");

    private UserDaoImpl userDao;
    private SessionFactory sessionFactory;

    @BeforeAll
    static void setupDatabase() {
        db.start();
    }

    @BeforeEach
    void setUp() {
        System.setProperty("hibernate.connection.url", db.getJdbcUrl());
        System.setProperty("hibernate.connection.username", db.getUsername());
        System.setProperty("hibernate.connection.password", db.getPassword());

        sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    static void tearDown() {
        db.stop();
        HibernateSessionFactoryUtil.shutdown();
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("Test User", "test@test.com", 25);
        userDao.save(user);

        User foundUser = userDao.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getAge(), foundUser.getAge());
    }

    @Test
    void testFindByEmail() {
        User user = new User("Test User", "test@test.com", 25);
        userDao.save(user);

        User foundUser = userDao.findByEmail("test@test.com");
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void testUpdate() {
        User user = new User("Test User", "test@test.com", 25);
        userDao.save(user);

        user.setName("Updated Name");
        userDao.update(user);

        User updatedUser = userDao.findById(user.getId());
        assertEquals("Updated Name", updatedUser.getName());
    }
}
