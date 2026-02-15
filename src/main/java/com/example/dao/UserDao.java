package com.example.dao;

import com.example.models.User;
import java.util.List;

public interface UserDao {
    User findById(Long id);
    User save(User user);
    void update(User user);
    void delete(User user);
    List<User> findAll();
    User findByEmail(String email);
}