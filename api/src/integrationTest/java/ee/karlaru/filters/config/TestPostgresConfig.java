package ee.karlaru.filters.config;

import org.postgresql.Driver;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@Configuration
@Testcontainers
public class TestPostgresConfig {

    @Container
    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16.3-alpine").asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("filters")
            .withUsername("karl")
            .withPassword("aru")
            .withExposedPorts(5432);

    @Bean
    @Primary
    @ConfigurationProperties("datasource.postgresql")
    public DataSourceProperties getDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource getDataSource() {
        postgres.start();

        String url = String.format("jdbc:postgresql://%s:%d/%s",
                postgres.getHost(),
                postgres.getMappedPort(5432),
                postgres.getDatabaseName()
                );
        getDatasourceProperties().setUrl(url);
        getDatasourceProperties().setDriverClassName(Driver.class.getName());

        return getDatasourceProperties().initializeDataSourceBuilder()
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .build();
    }
}
