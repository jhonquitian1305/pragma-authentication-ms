package co.com.pragma.api.dto;

import java.time.LocalDate;

public record ResponseUserDTO(
        String name,
        String lastname,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        double baseSalary) {
}
