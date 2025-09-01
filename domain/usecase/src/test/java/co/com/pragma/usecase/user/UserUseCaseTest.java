package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exception.DniExistsException;
import co.com.pragma.usecase.user.exception.EmailExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class UserUseCaseTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .name("John")
                .lastname("Doe")
                .dni("12345678")
                .birthDate(LocalDate.parse("2025-12-25"))
                .address("en casa")
                .phone("3254887894")
                .email("correo@mail.com")
                .baseSalary(1200.0)
                .build();
    }

    @Test
    void shouldSaveOneUserWhenEmailIsUnique(){
        when(this.userRepository.isUniqueEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(this.userRepository.existsByDni(user.getDni())).thenReturn(Mono.just(false));
        when(this.userRepository.saveOne(user)).thenReturn(Mono.just(user));

        StepVerifier.create(this.userUseCase.saveOne(user))
                .expectNext(user)
                .verifyComplete();

        verify(this.userRepository).isUniqueEmail(user.getEmail());
        verify(this.userRepository).existsByDni(user.getDni());
        verify(this.userRepository).saveOne(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists(){
        when(this.userRepository.isUniqueEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(this.userUseCase.saveOne(user))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof EmailExistsException;
                    assert ex.getMessage().equals("El email debe ser único");
                })
                .verify();

        verify(this.userRepository).isUniqueEmail(user.getEmail());
        verify(this.userRepository, never()).saveOne(any());
    }

    @Test
    void shouldThrowExceptionWhenDniExists(){
        when(this.userRepository.isUniqueEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(this.userRepository.existsByDni(user.getDni())).thenReturn(Mono.just(true));

        StepVerifier.create(this.userUseCase.saveOne(user))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof DniExistsException;
                    assert ex.getMessage().equals("El dni ingresado ya se encuentra registrado");
                })
                .verify();

        verify(this.userRepository).isUniqueEmail(user.getEmail());
        verify(this.userRepository).existsByDni(user.getDni());
        verify(this.userRepository, never()).saveOne(any());
    }


}
