package ee.karlaru.filters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FiltersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiltersApplication.class, args);
	}

}
