package co.com.pragma.model.user.validation;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.BusinessException;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserValidation {


    public void validate(User user){
        List<String> errors = new ArrayList<>();
        this.validateName(user.getName(), errors);
        this.validateLastName(user.getLastname(), errors);
        this.validateEmail(user.getEmail(), errors);
        this.validateBaseSalary(user.getBaseSalary(), errors);
        if(!errors.isEmpty()){
            throw new BusinessException("Error de ingreso en los datos ", errors);
        }
    }

    private void validateName(String name, List<String> errors) {
        if(this.isNullOrEmpty(name)){
            errors.add("Los nombres no puede ser nulo o vacío");
        }
    }

    private void validateLastName(String lastname, List<String> errors) {
        if(this.isNullOrEmpty(lastname)){
            errors.add("Los apellidos no pueden ser nulos o vacíos");
        }
    }

    private void validateEmail(String email, List<String> errors) {
        if(this.isNullOrEmpty(email)){
            errors.add("El email no puede ser nulo");
            return;
        }
        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            errors.add("Debe ser un email válido");
        }
    }

    private void validateBaseSalary(Double baseSalary, List<String> errors) {
        int minValue = 0;
        int maxValue = 15_000_000;
        if(baseSalary == null){
            errors.add("El salario base no puede ser nulo");
            return;
        }
        if(baseSalary <= minValue || baseSalary >= maxValue){
            errors.add(String.format("El salario debe estar entre %s y %s", minValue, maxValue));
        }

    }

    private boolean isNullOrEmpty(String data){
        return data == null || data.isEmpty();
    }
}
