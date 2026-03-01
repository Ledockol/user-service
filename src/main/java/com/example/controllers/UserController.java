package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private Long id;

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 100, message = "Длина имени должна быть от 2 до 100 символов")
    private String name;

    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 120, message = "Возраст не может превышать 120 лет")
    private Integer age;

    @NotNull(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    private String phone;

    @NotNull(message = "Роль не может быть пустой")
    private String role;

    // Дополнительные поля можно добавить по необходимости
    // Например:
    // private String address;
    // private String description;
}
