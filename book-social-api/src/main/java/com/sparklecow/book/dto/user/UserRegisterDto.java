package com.sparklecow.book.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserRegisterDto(
        @NotBlank(message = "Firstname is mandatory")
        @Size(min = 3, max = 50, message = "Firstname must be at least 3 characters and maximum 50")
        String firstName,
        @NotBlank(message = "Lastname is mandatory")
        @Size(min = 3, max = 50, message = "Lastname must be at least 3 characters and maximum 50")
        String lastName,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8)
        String password,
        @NotNull(message = "Date of birth is mandatory")
        LocalDate dateOfbirth
) {
}
