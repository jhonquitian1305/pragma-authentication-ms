package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.ResponseUserDTO;
import co.com.pragma.api.exception.GlobalExceptionFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @RouterOperation(
            operation = @Operation(
                operationId = "users", summary = "Save a user", tags = { "Users" },
                requestBody = @RequestBody(
                        required = true,
                        description = "dasfa",
                        content = @Content(
                                schema = @Schema(implementation = CreateUserDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "User example",
                                                value = """
                                                        {
                                                            "name": "John",
                                                            "lastname": "Doe",
                                                            "email": "jhon@example.com",
                                                            "birthDate": "2025-05-15",
                                                            "address": "dirección vivienda",
                                                            "phone": "1234567890",
                                                            "email" : "johndoe@example.com",
                                                            "baseSalary": 12000
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                responses = {
                    @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = ResponseUserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data or email exists"),
                }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/users"), handler::saveOne)
                .filter(filter);
    }
}
