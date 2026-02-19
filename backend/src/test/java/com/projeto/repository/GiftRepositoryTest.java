package com.projeto.repository;

import com.projeto.model.Gift;
import com.projeto.model.GiftStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use actual DB or configured test DB
public class GiftRepositoryTest {

    @Autowired
    private GiftRepository giftRepository;

    @Test
    public void save_ShouldPersistCategory() {
        // Arrange
        Gift gift = Gift.builder()
                .id(UUID.randomUUID())
                .name("Repo Test Gift")
                .category("RepoCategory")
                .status(GiftStatus.AVAILABLE)
                .visible(true)
                .build();

        // Act
        Gift savedGift = giftRepository.save(gift);

        // Assert
        Assertions.assertNotNull(savedGift);
        Assertions.assertEquals("RepoCategory", savedGift.getCategory());

        // Verify retrieval
        Gift retrievedGift = giftRepository.findById(savedGift.getId()).orElseThrow();
        Assertions.assertEquals("RepoCategory", retrievedGift.getCategory());
    }
}
