package co.com.pragma.api.dto;

import java.time.LocalDate;

public record ResponseUserDTO(
        Long id,
        String name,
        String lastname,
        String dni,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        double baseSalary) {
}
