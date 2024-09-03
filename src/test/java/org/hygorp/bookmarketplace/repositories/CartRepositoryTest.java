package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.entities.CartEntity;
import org.hygorp.bookmarketplace.enums.Condition;
import org.hygorp.bookmarketplace.enums.CoverType;
import org.hygorp.bookmarketplace.enums.Language;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CartRepositoryTests")
public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void beforeEach() {
        cartRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        cartRepository.deleteAll();
    }

    @Test
    @DisplayName("should save cart")
    @Order(1)
    void shouldSaveCart() {
        CartEntity cart = Assertions.assertDoesNotThrow(() -> cartRepository.save(new CartEntity()));

        Assertions.assertNotNull(cart);
        Assertions.assertNotNull(cart.getId());
    }

    @Test
    @DisplayName("should find cart by id")
    @Order(2)
    void ShouldFindCartById() {
        CartEntity cart = Assertions.assertDoesNotThrow(() -> cartRepository.save(new CartEntity()));

        UUID myCartId = cart.getId();

        CartEntity savedCart = Assertions.assertDoesNotThrow(() -> cartRepository.findById(myCartId).orElseThrow());

        Assertions.assertEquals(myCartId, savedCart.getId());
    }

    @Test
    @DisplayName("should update cart")
    @Order(3)
    void shouldUpdateCart() {
        CartEntity cart = Assertions.assertDoesNotThrow(() -> cartRepository.save(new CartEntity()));

        UUID myCartId = cart.getId();

        CartEntity savedCart = Assertions.assertDoesNotThrow(() -> cartRepository.findById(myCartId).orElseThrow());

        Assertions.assertEquals(0, savedCart.getItems().size());

        savedCart.getItems().add(bookRepository.save(new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "1012242628",
                "https://image.com/book-test-01.jpg",
                44.90,
                10,
                Condition.NEW,
                CoverType.HARDCOVER,
                Language.EN_US
        )));

        CartEntity updatedCart = Assertions.assertDoesNotThrow(() -> cartRepository.save(savedCart));
        Assertions.assertEquals(1, updatedCart.getItems().size());
        Assertions.assertEquals(44.90, updatedCart.totalValue());
    }

    @Test
    @DisplayName("should delete cart")
    @Order(4)
    void shouldDeleteCart() {
        CartEntity cart = Assertions.assertDoesNotThrow(() -> cartRepository.save(new CartEntity()));

        UUID myCartId = cart.getId();

        cartRepository.deleteById(myCartId);

        Assertions.assertThrows(NoSuchElementException.class, () -> cartRepository.findById(myCartId).orElseThrow());
    }
}
