package ee.karlaru.filters;

import ee.karlaru.filters.domain.UserRole;
import ee.karlaru.filters.util.TokenVerifier;
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
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("itest")
class AuthenticationTests {

	private final Path createFilter = Path.of("src/integrationTest/resources/fixtures/create-filter.json");

	@Autowired private MockMvc mvc;
	@Autowired private TokenVerifier tokenVerifier;


	@Test
	void shouldReturnReadOnlyRoleJwtToken() throws Exception {
		String jwtToken = mvc.perform(get("/api/v1/auth/read"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andReturn().getResponse().getContentAsString();

		assertThat(jwtToken).isNotNull();
        assertEquals(List.of(UserRole.READ.name()), tokenVerifier.getRoles(jwtToken));


		// Test JwtAuthenticationProvider
		mvc.perform(get("/api/v1/classifications")
				.header("Authorization", "Bearer " + jwtToken))
				.andExpect(MockMvcResultMatchers.status().isOk());

    }

	@Test
	void shouldReturnReadWriteRoleJwtToken() throws Exception {
		String jwtToken = mvc.perform(get("/api/v1/auth/write"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andReturn().getResponse().getContentAsString();

		assertThat(jwtToken).isNotNull();
		assertEquals(
				new HashSet<>(List.of(UserRole.WRITE.name(), UserRole.READ.name())),
				new HashSet<>(tokenVerifier.getRoles(jwtToken)));

	}

	@Test
	@WithMockUser(roles = {"READ"})
	void shouldFailAuthorization() throws Exception {
		final String newFilter = Files.readString(createFilter);

		mvc.perform(patch("/api/v1/filter")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newFilter))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"message\":\"Access Denied\",\"code\":\"ACCESS_DENIED\"}"));
	}

}
