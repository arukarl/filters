package ee.karlaru.filters;

import ee.karlaru.filters.repository.FilterRepository;
import ee.karlaru.filters.util.TestDataFactory;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("itest")
class FilterTests {

	public static final String FILTER_PATH = "/api/v1/filter";

	private final Path filters = Path.of("src/integrationTest/resources/fixtures/filters.json");
	private final Path oneFilter = Path.of("src/integrationTest/resources/fixtures/one-filter.json");
	private final Path createFilter = Path.of("src/integrationTest/resources/fixtures/create-filter.json");

	@Autowired private MockMvc mvc;
	@Autowired private TestDataFactory testDataFactory;
	@Autowired private FilterRepository filterRepository;

	@BeforeEach
	void setUp() {
		testDataFactory.createTestFilters();
	}

	@AfterEach
	void tearDown() {
		testDataFactory.cleanUp();
	}


	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnAllFilters() throws Exception {
		String expectedFilters = Files.readString(filters);

		mvc.perform(get(FILTER_PATH))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedFilters));
    }

	@Test
	@WithMockUser(roles = "READ")
	void shouldReturnOneFilter() throws Exception {
		String expectedFilter = Files.readString(oneFilter);

		mvc.perform(get(FILTER_PATH + "/" + TestDataFactory.SECOND_FILTER_UUID))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedFilter));
	}

	@Test
	@WithMockUser(roles = "READ")
	void shouldNotFindFilter() throws Exception {
		String uuid = "7b9dd385-7541-4d1b-8e74-94ba5b4b868b";

		mvc.perform(get(FILTER_PATH + "/" + uuid))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(
						"{\"message\":\"Filter 7b9dd385-7541-4d1b-8e74-94ba5b4b868b not found\"," +
								"\"code\":\"FILTER_NOT_FOUND\"}"));
	}

	@Test
	@WithMockUser(roles = {"WRITE","READ"})
	void shouldCreateFilter() throws Exception {
		final String newFilter = Files.readString(createFilter);

		String result = mvc.perform(patch(FILTER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(newFilter))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		UUID responseUUID = stringToUuid(result);

		mvc.perform(get(FILTER_PATH + "/" + responseUUID))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(newFilter));
	}

	@Test
	@WithMockUser(roles = {"WRITE","READ"})
	void shouldUpdateFilter() throws Exception {
		String filterJson = Files.readString(oneFilter);
		filterJson = filterJson.replace("\"targetValue\": \"movie\"", "\"targetValue\": \"new-movie\"");

		mvc.perform(patch(FILTER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(filterJson))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		assertTrue(filterRepository.findByUuid(TestDataFactory.SECOND_FILTER_UUID).isEmpty());
	}

	@Test
	@WithMockUser(roles = {"WRITE","READ"})
	void shouldFailCriterionValidation() throws Exception {
		String filterJson = Files.readString(oneFilter);
		filterJson = filterJson.replace("\"operator\": \"CONTAINS\"", "\"operator\": \"WRONG\"");

		doBadRequest(filterJson, "{\"message\":\"Unknown operator WRONG\"}");
	}

	@Test
	@WithMockUser(roles = {"WRITE","READ"})
	void shouldFailFieldValidation() throws Exception {
		String filterJson = Files.readString(oneFilter);
		filterJson = filterJson.replace("\"targetField\": \"title\"", "\"targetField\": \"WRONG\"");

		doBadRequest(filterJson, "{\"message\":\"Unknown field WRONG\"}");
	}

	private void doBadRequest(String content, String expectedJson) throws Exception {
		mvc.perform(patch(FILTER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(content))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedJson));
	}

	private static @NotNull UUID stringToUuid(String result) {
		return UUID.fromString(result.replace("\"", ""));
	}

}
