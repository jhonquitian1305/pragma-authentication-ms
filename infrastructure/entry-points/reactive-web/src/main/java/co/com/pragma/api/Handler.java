package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.mapper.UserDTOMapper;
import co.com.pragma.usecase.user.IUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final IUserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;

    public Mono<ServerResponse> saveOne(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(createUserDTO -> log.info("beginning register a user"))
                .map(this.userDTOMapper::toModel)
                .flatMap(this.userUseCase::saveOne)
                .map(this.userDTOMapper::toResponse)
                .doOnNext(responseUserDTO -> log.info("user registered"))
                .flatMap(responseUserDTO ->
                        ServerResponse.created(URI.create("/api/v1/users")).bodyValue(responseUserDTO));
    }

    public Mono<ServerResponse> getByDni(ServerRequest serverRequest){
        String dni = serverRequest.pathVariable("dni");

        return this.userUseCase.getByDni(dni)
                .map(this.userDTOMapper::toResponse)
                .flatMap(responseUserDTO -> ServerResponse.ok().bodyValue(responseUserDTO));
    }
}
