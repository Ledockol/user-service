package com.example.repositories;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByRole(String role);
    List<User> findByNameContaining(String name);
    List<User> findByAge(Integer age);
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query("SELECT u FROM User u WHERE u.registrationDate >= :date")
    List<User> findUsersRegisteredAfter(@Param("date") LocalDate date);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames")
    List<User> findByRoleNames(@Param("roleNames") List<String> roleNames);
}
