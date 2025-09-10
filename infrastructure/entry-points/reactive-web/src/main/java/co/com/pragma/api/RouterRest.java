package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.LoginDTO;
import co.com.pragma.api.dto.ResponseUserDTO;
import co.com.pragma.api.exception.GlobalExceptionFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @RouterOperation(
            method = RequestMethod.POST,
            operation = @Operation(
                    summary = "Login", operationId = "login", tags = { "Login" },
                    requestBody = @RequestBody(
                            required = true,
                            description = "Login",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    )

            )
    )
    @Bean
    public RouterFunction<ServerResponse> loginRoute(Handler authHandler, GlobalExceptionFilter filter) {
        return route(
                POST("/api/v1/login"), authHandler::logIn)
                .filter(filter);
    }

    @RouterOperation(
            operation = @Operation(
                operationId = "users", summary = "Save a user", tags = { "Users" },
                requestBody = @RequestBody(
                        required = true,
                        description = "Create a user",
                        content = @Content(
                                schema = @Schema(implementation = CreateUserDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "User example",
                                                value = """
                                                        {
                                                            "name": "John",
                                                            "lastname": "Doe",
                                                            "dni": "123456789",
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
                },
                security = { @SecurityRequirement(name = "bearerAuth")}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, GlobalExceptionFilter globalExceptionFilter) {
        return route(POST("/api/v1/users"), handler::saveOne)
                .filter(globalExceptionFilter);
    }

    @RouterOperation(
            method = RequestMethod.GET,
            operation = @Operation(
                    summary = "Get user by dni for create a loan", operationId = "users", tags = { "Users" },
                    parameters = {
                            @Parameter(
                                    name = "dni",
                                    in = ParameterIn.PATH,
                                    description = "dni user",
                                    required = true
                            )
                    },
                    security = { @SecurityRequirement(name = "bearerAuth")}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> routerGetFunction(Handler handler, GlobalExceptionFilter filter) {
        return route(GET("/api/v1/users/{dni}"), handler::getByDni)
                .filter(filter);
    }

    @RouterOperation(
            method = RequestMethod.GET,
            operation = @Operation(
                    summary = "Get info user by dni for get all loans", operationId = "users", tags = { "Users" },
                    parameters = {
                            @Parameter(
                                    name = "dni",
                                    in = ParameterIn.PATH,
                                    description = "dni user",
                                    required = true
                            )
                    },
                    security = { @SecurityRequirement(name = "bearerAuth")}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> userInfoDni(Handler handler, GlobalExceptionFilter filter){
        return route( GET("/api/v1/users/info/{dni}"), handler::userInfoDni)
                .filter(filter);
    }
}
