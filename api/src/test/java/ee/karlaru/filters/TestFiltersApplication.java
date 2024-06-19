package ee.karlaru.filters;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestFiltersApplication {

	public static void main(String[] args) {
		SpringApplication.from(FiltersApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
