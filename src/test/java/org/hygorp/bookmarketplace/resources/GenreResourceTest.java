package org.hygorp.bookmarketplace.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.hygorp.bookmarketplace.repositories.GenreRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("GenreResourceTests")
public class GenreResourceTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MockMvc mockMvc;

    private UUID myGenreId01;
    private UUID myGenreId02;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        myGenreId01 = genreRepository.save(
                new GenreEntity(
                        "Fiction", "https://image.com/fiction.jpg"

                )
        ).getId();

        myGenreId02 = genreRepository.save(
                new GenreEntity(
                        "Romance", "https://image.com/romance.jpg"
                )
        ).getId();
    }

    @AfterEach
    void afterEach() {
        genreRepository.deleteAll();
    }

    @Test
    @DisplayName("should return two genres and http 200 status")
    @Order(1)
    void shouldReturnTwoGenresAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/genres/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("content").size());
    }

    @Test
    @DisplayName("should return genre by id and http 200 status")
    @Order(2)
    void shouldReturnGenreByIdAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/genres/find-by-id/" + myGenreId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myGenreId01, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Fiction", body.get("name").asText());
    }

    @Test
    @DisplayName("should return genres by title and http 200 status")
    @Order(3)
    void shouldReturnGenresByTitleAndHttp200Status() {
        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/genres/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "Fiction"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            System.out.println(body);

            Assertions.assertEquals(1, body.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/genres/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "Romance"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            System.out.println(body);

            Assertions.assertEquals(1, body.size());
        });
    }

    @Test
    @DisplayName("should save genre and return http 201 status")
    @Order(4)
    void shouldSaveGenreAndReturnHttp201Status() throws Exception {
        MvcResult response = mockMvc.perform(post("/api/v1/genres/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GenreEntity(
                                "Genre Test 03",
                                "https://image.com/genre-test-03.jpg"
                        ))))
                .andExpect(status().isCreated()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals("Genre Test 03", body.get("name").asText());
    }

    @Test
    @DisplayName("should update genre and return http 200 status")
    @Order(5)
    void shouldUpdateGenreAndReturnHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(put("/api/v1/genres/update/" + myGenreId02)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GenreEntity(
                                "Romance - (Edited)",
                                "https://image.com/romance.jpg"
                        ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myGenreId02, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Romance - (Edited)", body.get("name").asText());
        Assertions.assertEquals("https://image.com/romance.jpg", body.get("image").asText());
    }

    @Test
    @DisplayName("should delete genre and return http 204 status")
    @Order(6)
    void shouldDeleteGenreAndReturnHttp204Status() throws Exception {
        mockMvc.perform(delete("/api/v1/genres/delete/" + myGenreId01)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }
}
