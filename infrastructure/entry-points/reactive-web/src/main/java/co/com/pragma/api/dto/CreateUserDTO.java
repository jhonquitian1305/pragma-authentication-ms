package co.com.pragma.api.dto;

import java.time.LocalDate;


public record CreateUserDTO(
        String name,
        String lastname,
        String dni,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        double baseSalary) {
}
