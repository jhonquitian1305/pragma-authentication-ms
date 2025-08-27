package co.com.pragma.r2dbc.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
public class PostgreSQLConnectionPool {

    @Bean
    public ConnectionFactory connectionFactory(PostgresqlConnectionProperties props) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgres")
                .option(HOST, props.host())
                .option(PORT, props.port())
                .option(USER, props.username())
                .option(PASSWORD, props.password())
                .option(DATABASE, props.database())
                .option(MAX_SIZE, props.maxSize())
                .option(INITIAL_SIZE, props.initialSize())
                .option(MAX_IDLE_TIME, Duration.parse("PT" + props.maxIdleTime().toUpperCase()))
                .build();

        return ConnectionFactories.get(options);
    }
}