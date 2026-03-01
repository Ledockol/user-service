package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@Validated
public class UserDto {
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Min(1)
    @Max(120)
    private Integer age;
}