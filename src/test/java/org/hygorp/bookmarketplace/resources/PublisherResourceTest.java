package org.hygorp.bookmarketplace.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.bookmarketplace.entities.PublisherEntity;
import org.hygorp.bookmarketplace.repositories.PublisherRepository;
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
@DisplayName("PublisherResourceTests")
public class PublisherResourceTest {
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private MockMvc mockMvc;

    private UUID myPublisherId01;
    private UUID myPublisherId02;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        myPublisherId01 = publisherRepository.save(
                new PublisherEntity(
                        "Publisher Test 01", "https://image.com/publisher-01-test.jpg"

                )
        ).getId();

        myPublisherId02 = publisherRepository.save(
                new PublisherEntity(
                        "Publisher Test 02", "https://image.com/publisher-02-test.jpg"
                )
        ).getId();
    }

    @AfterEach
    void afterEach() {
        publisherRepository.deleteAll();
    }

    @Test
    @DisplayName("should return two publishers and http 200 status")
    @Order(1)
    void shouldReturnTwoPublishersAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/publishers/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("content").size());
    }

    @Test
    @DisplayName("should return publisher by id and http 200 status")
    @Order(2)
    void shouldReturnPublisherByIdAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/publishers/find-by-id/" + myPublisherId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myPublisherId01, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Publisher Test 01", body.get("name").asText());
    }

    @Test
    @DisplayName("should return publishers by title and http 200 status")
    @Order(3)
    void shouldReturnPublishersByTitleAndHttp200Status() {
        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/publishers/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "Publisher Test 01"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            Assertions.assertEquals(1, body.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            MvcResult response = mockMvc.perform(get("/api/v1/publishers/find-by-name")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", "Publisher Test 02"))
                    .andExpect(status().isOk()).andReturn();

            JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

            Assertions.assertEquals(1, body.size());
        });
    }

    @Test
    @DisplayName("should save publisher and return http 201 status")
    @Order(4)
    void shouldSavePublisherAndReturnHttp201Status() throws Exception {
        MvcResult response = mockMvc.perform(post("/api/v1/publishers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PublisherEntity(
                                "Publisher Test 03",
                                "https://image.com/publisher-03-test.jpg"
                        ))))
                .andExpect(status().isCreated()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals("Publisher Test 03", body.get("name").asText());
    }

    @Test
    @DisplayName("should update publisher and return http 200 status")
    @Order(5)
    void shouldUpdatePublisherAndReturnHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(put("/api/v1/publishers/update/" + myPublisherId02)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PublisherEntity(
                        "Publisher Test 02 - (Edited)",
                        "https://image.com/publisher-02-test.jpg"
                ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myPublisherId02, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Publisher Test 02 - (Edited)", body.get("name").asText());
        Assertions.assertEquals("https://image.com/publisher-02-test.jpg", body.get("logo").asText());
    }

    @Test
    @DisplayName("should delete publisher and return http 204 status")
    @Order(6)
    void shouldDeletePublisherAndReturnHttp204Status() throws Exception {
        mockMvc.perform(delete("/api/v1/publishers/delete/" + myPublisherId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }
}
