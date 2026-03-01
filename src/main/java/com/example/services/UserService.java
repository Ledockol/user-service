package com.example.services;

import com.example.dto.UserDto;
import com.example.exceptions.ResourceNotFoundException;
import com.example.models.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = convertDtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRegistrationDate(LocalDate.now());
        return convertEntityToDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertEntityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDto.getName());
                    user.setEmail(userDto.getEmail());
                    user.setAge(userDto.getAge());
                    user.setPhone(userDto.getPhone());
                    if (userDto.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    }
                    return convertEntityToDto(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertEntityToDto);
    }

    private User convertDtoToEntity(UserDto userDto) {
        User user = new User();
        user.setId(UUID.randomUUID().getMostSignificantBits());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        return user;
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
