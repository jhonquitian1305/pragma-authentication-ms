package co.com.pragma.api.exception;

import co.com.pragma.model.user.exception.BusinessException;
import co.com.pragma.usecase.user.exception.EmailExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(EmailExistsException.class, ex -> {
                    log.error("EmailExistsException: {}", ex.getMessage(), ex);

                    return ServerResponse.status(400).bodyValue(
                            ErrorResponse.builder()
                                    .tittle("Email exists error")
                                    .message(ex.getMessage())
                                    .status(400)
                                    .build()
                    );
                })
                .onErrorResume(BusinessException.class, ex -> {
                    log.error("BusinessException: {}", ex.getMessage(), ex);

                    return ServerResponse.status(ex.getStatus()).bodyValue(
                            ErrorResponse.builder()
                                    .tittle(ex.getTitle())
                                    .status(ex.getStatus())
                                    .message(ex.getMessage())
                                    .build()
                    );
                })
                .onErrorResume(Throwable.class, ex -> {
                    log.error("Unexpected error: {}", ex.getMessage(), ex);

                    return ServerResponse.status(500).bodyValue(
                            ErrorResponse.builder()
                                    .tittle("Internal Server Error")
                                    .message("Ocurrió un error inesperado, intente más tarde")
                                    .status(500)
                                    .build()
                    );
                });
    }
}
