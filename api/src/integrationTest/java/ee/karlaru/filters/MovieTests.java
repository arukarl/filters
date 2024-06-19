package ee.karlaru.filters;

import ee.karlaru.filters.util.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("itest")
class MovieTests {

	private final Path allMovies = Path.of("src/integrationTest/resources/fixtures/movies.json");
	private final Path filteredMovies = Path.of("src/integrationTest/resources/fixtures/filtered-movies.json");

	@Autowired private MockMvc mvc;
	@Autowired private TestDataFactory testDataFactory;

	@BeforeEach
	void setUp() {
		testDataFactory.createTestMovies();
		testDataFactory.createTestFilters();
	}

	@AfterEach
	void tearDown() {
		testDataFactory.cleanUp();
	}

	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnAllMovies() throws Exception {
		String expectedMovies = Files.readString(allMovies);

		mvc.perform(get("/api/v1/movie"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedMovies));
    }

	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnFilteredMovies() throws Exception {
		String expectedMovies = Files.readString(filteredMovies);

		mvc.perform(get("/api/v1/movie/filtered").param("filter", TestDataFactory.FIRST_FILTER_UUID.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedMovies));
	}

	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnErrorFilterNotFound() throws Exception {

		mvc.perform(get("/api/v1/movie/filtered").param("filter", "f4eb74bd-9e6d-409b-9bef-6928d7de25e3"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\":\"Filter f4eb74bd-9e6d-409b-9bef-6928d7de25e3 not found\"," +
						"\"code\":\"FILTER_NOT_FOUND\"}"));
	}

	@Test
	@WithMockUser(roles = "WRITE")
	void shouldSaveMovie() throws Exception {
		String movie = "{\"title\":\"The Matrix\",\"views\":\"200\",\"releaseDate\":\"2024-06-18T21:47:04.977Z\"}";

		mvc.perform(post("/api/v1/movie")
						.content(movie)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
