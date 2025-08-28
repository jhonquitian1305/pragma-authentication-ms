package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter userReactiveRepositoryAdapter;

    @Mock
    UserReactiveRepository userReactiveRepository;

    @Mock
    ObjectMapper mapper;

    private User user;
    private UserEntity userEntity;

   @BeforeEach()
    void setup(){
       MockitoAnnotations.openMocks(this);
       user = User.builder()
               .id(1L)
               .name("John")
               .lastname("Doe")
               .birthDate(LocalDate.parse("2025-12-25"))
               .address("en casa")
               .phone("3254887894")
               .email("correo@mail.com")
               .baseSalary(1200.0)
               .isActive(true)
               .build();

       userEntity = UserEntity.builder()
               .id(1L)
               .name("John")
               .lastname("Doe")
               .birthDate(LocalDate.parse("2025-12-25"))
               .address("en casa")
               .phone("3254887894")
               .email("correo@mail.com")
               .baseSalary(1200.0)
               .isActive(true)
               .build();
   }

   @Test
    void shouldSaveOneUser(){
       when(this.mapper.map(user, UserEntity.class)).thenReturn(userEntity);
       when(this.userReactiveRepository.save(userEntity)).thenReturn(Mono.just(userEntity));
       when(this.mapper.map(userEntity, User.class)).thenReturn(user);

       StepVerifier.create(userReactiveRepositoryAdapter.saveOne(user))
                .expectNext(user)
                .verifyComplete();

        verify(mapper).map(user, UserEntity.class);
        verify(this.userReactiveRepository).save(userEntity);
        verify(mapper).map(userEntity, User.class);
   }
}
