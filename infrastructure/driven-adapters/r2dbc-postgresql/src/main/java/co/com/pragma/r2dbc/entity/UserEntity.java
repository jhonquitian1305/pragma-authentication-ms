package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("users")
public class UserEntity {

    @Id
    private Long id;

    private String name;
    private String lastname;
    private String dni;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private Double baseSalary;
    private Boolean isActive;
}
