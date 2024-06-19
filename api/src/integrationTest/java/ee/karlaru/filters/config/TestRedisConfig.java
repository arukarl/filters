package ee.karlaru.filters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Configuration
public class TestRedisConfig {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>("redis:7.2.5-alpine")
            .withExposedPorts(6379);

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        redis.start();

        return new LettuceConnectionFactory(
                redis.getHost(),
                redis.getMappedPort(6379));
    }
}
