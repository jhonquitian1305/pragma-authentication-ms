package co.com.pragma.model.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private final String title;
    private final int status = 400;

    public BusinessException(String title, List<String> errors) {
        super(String.join(",", errors));
        this.title = title;
    }
}
