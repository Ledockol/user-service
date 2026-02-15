package com.example.services;

import com.example.models.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    User findByEmail(String email);
    List<User> findUsersByName(String name);
    List<User> findUsersByAge(int age);
}