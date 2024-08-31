package org.hygorp.bookmarketplace.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.SellerEntity;
import org.hygorp.bookmarketplace.repositories.SellerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("SellerResourceTests")
public class SellerResourceTest {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        sellerRepository.saveAll(Arrays.asList(
                new SellerEntity(
                        "Seller Test 01",
                        "558132373231",
                        "https://logo.com/seller-01.jpg",
                        Instant.now(),
                        new AddressEntity(
                                "Rua Direita, 156",
                                "Recife",
                                "PE",
                                "51100000",
                                "Brasil",
                                ""
                        )
                ),
                new SellerEntity(
                        "Seller Test 02",
                        "558131315958",
                        "https://logo.com/seller-02.jpg",
                        Instant.now(),
                        new AddressEntity(
                                "Rua Esquerda, 561",
                                "Recife",
                                "PE",
                                "51100001",
                                "Brasil",
                                ""
                        )
                )
        ));
    }

    @AfterEach
    void afterEach() {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName("should return two sellers and http 200 status")
    @Order(1)
    void shouldReturnTwoSellersAndHttp200Status() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/v1/sellers/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(response.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("content").size());
    }
}
