package com.example.services;

import com.example.dao.UserDao;
import com.example.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        if (user == null || user.getName() == null || user.getEmail() == null || user.getAge() == null) {
            logger.error("Некорректные данные для создания пользователя");
            return null;
        }
        try {
            userDao.save(user);
            logger.info("Пользователь создан: {}", user);
            return user;
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя", e);
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            logger.warn("Пользователь с ID {} не найден", id);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.findAll();
        if (users == null || users.isEmpty()) {
            logger.warn("Список пользователей пуст");
        }
        return users;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            logger.error("Не указан ID пользователя для обновления");
            return null;
        }
        try {
            userDao.update(user);
            logger.info("Пользователь обновлен: {}", user);
            return user;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя", e);
            return null;
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = userDao.findById(id);
        if (user != null) {
            try {
                userDao.delete(user);
                logger.info("Пользователь с ID {} удален", id);
            } catch (Exception e) {
                logger.error("Ошибка при удалении пользователя с ID {}", id, e);
            }
        } else {
            logger.warn("Пользователь с ID {} не найден для удаления", id);
        }
    }

    @Override
    public User findByEmail(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            logger.warn("Пользователь с email {} не найден", email);
        }
        return user;
    }

    @Override
    public List<User> findUsersByName(String name) {
        return userDao.findAll()
                .stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public List<User> findUsersByAge(int age) {
        return userDao.findAll()
                .stream()
                .filter(u -> u.getAge() == age)
                .toList();
    }
}