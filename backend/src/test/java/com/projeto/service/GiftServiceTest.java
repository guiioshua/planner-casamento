package com.projeto.service;

import com.projeto.dto.GiftRequest;
import com.projeto.dto.GiftResponse;
import com.projeto.model.Gift;
import com.projeto.model.GiftStatus;
import com.projeto.repository.GiftRepository;
import com.projeto.repository.InvitationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @InjectMocks
    private GiftService giftService;

    @Test
    public void create_ShouldSaveGiftWithCorrectCategory() {
        // Arrange
        GiftRequest request = new GiftRequest();
        request.setName("Service Test Gift");
        request.setCategory("ServiceCategory");
        request.setStatus(GiftStatus.AVAILABLE);

        when(giftRepository.save(any(Gift.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GiftResponse response = giftService.create(request);

        // Assert
        Assertions.assertEquals("ServiceCategory", response.getCategory());
        verify(giftRepository).save(any(Gift.class));
    }
}
