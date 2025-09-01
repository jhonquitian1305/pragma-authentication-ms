package co.com.pragma.model.user;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class User {
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
