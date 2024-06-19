package ee.karlaru.filters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("itest")
class ClassificationTests {

	private final Path classificationsPath = Path.of("src/integrationTest/resources/fixtures/classifications.json");

	@Autowired private MockMvc mvc;

	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnClassifications() throws Exception {
		String expectedClassifications = Files.readString(classificationsPath);

		mvc.perform(get("/api/v1/classifications"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedClassifications));
    }

}
