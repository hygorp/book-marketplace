package org.hygorp.bookmarketplace.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.hygorp.bookmarketplace.repositories.AuthorRepository;
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
@DisplayName("AuthorResourceTests")
public class AuthorResourceTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;

    private UUID myAuthorId01;
    private UUID myAuthorId02;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {

        myAuthorId01 = authorRepository.save(
                new AuthorEntity(
                        "George Orwell",
                        "Eric Arthur Blair, better known by the pseudonym George Orwell, was an English writer, journalist and political essayist, born in British India",
                        "https://image.com/george_orwell.jpg"
                )
        ).getId();

        myAuthorId02 = authorRepository.save(
                new AuthorEntity(
                        "J.K Rowling",
                        "Joanne 'Jo' Rowling, better known as J. K. Rowling, is a British writer, screenwriter and film producer, known for writing the Harry Potter book series.",
                        "https://image.com/jk_rowling.jpg"
                )
        ).getId();
    }

    @AfterEach
    void afterEach() {
        authorRepository.deleteAll();
    }

    @Test
    @DisplayName("should return two authors and http 200 status")
    @Order(1)
    void shouldReturnTwoAuthorsAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/authors/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("content").size());
    }

    @Test
    @DisplayName("should return author by id and http 200 status")
    @Order(2)
    void shouldReturnAuthorByIdAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/authors/find-by-id/" + myAuthorId01)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myAuthorId01, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("George Orwell", body.get("name").asText());
    }

    @Test
    @DisplayName("should return authors by name and http 200 status")
    @Order(3)
    void shouldReturnAuthorsByNameAndHttp200Status() {
        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/authors/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "George Orwell"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            Assertions.assertEquals(1, body.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/authors/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "J.K Rowling"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            Assertions.assertEquals(1, body.size());
        });
    }

    @Test
    @DisplayName("should save author and return http 201 status")
    @Order(4)
    void shouldSaveAuthorAndReturnHttp201Status() throws Exception {
        MvcResult response = mockMvc.perform(post("/api/v1/authors/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthorEntity(
                                "Author Test 03",
                                "Bio Test 03",
                                "https://image.com/author_test_03.jpg"
                        ))))
                .andExpect(status().isCreated()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals("Author Test 03", body.get("name").asText());
    }

    @Test
    @DisplayName("should update author and return http 200 status")
    @Order(5)
    void shouldUpdateAuthorAndReturnHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(put("/api/v1/authors/update/" + myAuthorId02)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthorEntity(
                                "J.K Rowling - (Edited)",
                                "Resumed Bio, he's a british writer",
                                "https://image.com/jk_rowling.jpg"
                        ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myAuthorId02, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("J.K Rowling - (Edited)", body.get("name").asText());
        Assertions.assertEquals("Resumed Bio, he's a british writer", body.get("biography").asText());
        Assertions.assertEquals("https://image.com/jk_rowling.jpg", body.get("image").asText());
    }

    @Test
    @DisplayName("should delete author and return http 204 status")
    @Order(6)
    void shouldDeleteAuthorAndReturnHttp204Status() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/delete/" + myAuthorId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }
}
