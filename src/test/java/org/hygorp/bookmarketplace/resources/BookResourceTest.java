package org.hygorp.bookmarketplace.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.enums.Condition;
import org.hygorp.bookmarketplace.enums.CoverType;
import org.hygorp.bookmarketplace.repositories.BookRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BookResourceTests")
public class BookResourceTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    private UUID myBookId01;
    private UUID myBookId02;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        bookRepository.deleteAll();

        BookEntity book01 = new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "123456",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        );

        BookEntity book02 = new BookEntity(
                "Book Test 02",
                "Description Test 02",
                LocalDate.parse("1970-01-01"),
                "654321",
                "https://image.com/book-test-02.jpg",
                10,
                Condition.USED,
                CoverType.SOFTCOVER
        );

        myBookId01 = bookRepository.save(book01).getId();
        myBookId02 = bookRepository.save(book02).getId();
    }

    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("should return two books and http 200 status")
    @Order(1)
    void shouldReturnTwoBooksAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/books/find-all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("content").size());
    }

    @Test
    @DisplayName("should return book by id and http 200 status")
    @Order(2)
    void shouldReturnBookByIdAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/books/find-by-id/" + myBookId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myBookId01, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Book Test 01", body.get("title").asText());
    }

    @Test
    @DisplayName("should return books by title and http 200 status")
    @Order(3)
    void shouldReturnBooksByTitleAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/books/find-by-title")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "Book Test"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        System.out.println(body);

        Assertions.assertEquals(2, body.size());
    }

    @Test
    @DisplayName("should save book and return http 201 status")
    @Order(4)
    void shouldSaveBookAndReturnHttp201Status() throws Exception {
        MvcResult response = mockMvc.perform(post("/api/v1/books/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BookEntity(
                        "Book Test 03",
                        "Description Test 03",
                        LocalDate.parse("1975-01-06"),
                        "789987",
                        "https://image.com/book-test-03.jpg",
                        1,
                        Condition.USED,
                        CoverType.HARDCOVER
                ))))
                .andExpect(status().isCreated()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals("Book Test 03", body.get("title").asText());
    }

    @Test
    @DisplayName("should update book and return http 200 status")
    @Order(5)
    void shouldUpdateBookAndReturnHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(put("/api/v1/books/update/" + myBookId02)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new BookEntity(
                        "Book Test 02 - (Edited)",
                        "Updated Description Test 02",
                        LocalDate.parse("1970-01-01"),
                        "654321-2",
                        "https://image.com/book-test-02.jpg",
                        4,
                        Condition.USED,
                        CoverType.SOFTCOVER
                ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = new ObjectMapper().readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(myBookId02, UUID.fromString(body.get("id").asText()));
        Assertions.assertEquals("Book Test 02 - (Edited)", body.get("title").asText());
        Assertions.assertEquals("Updated Description Test 02", body.get("description").asText());
        Assertions.assertEquals("1970-01-01", body.get("publishedDate").asText());
        Assertions.assertEquals("654321-2", body.get("isbn").asText());
        Assertions.assertEquals("https://image.com/book-test-02.jpg", body.get("image").asText());
        Assertions.assertEquals(4, body.get("stock").asInt());
        Assertions.assertEquals("USED", body.get("condition").asText());
        Assertions.assertEquals("SOFTCOVER", body.get("coverType").asText());
    }

    @Test
    @DisplayName("should delete book and return http 204 status")
    @Order(6)
    void shouldDeleteBookAndReturnHttp204Status() throws Exception {
        mockMvc.perform(delete("/api/v1/books/delete/" + myBookId01)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }
}
