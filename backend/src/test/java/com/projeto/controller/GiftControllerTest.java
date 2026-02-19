package com.projeto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.dto.GiftRequest;
import com.projeto.dto.GiftResponse;
import com.projeto.model.GiftStatus;
import com.projeto.service.GiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftController.class)
public class GiftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftService giftService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createGift_ShouldPassCategoryToService() throws Exception {
        // Arrange
        GiftRequest request = new GiftRequest();
        request.setName("Test Gift");
        request.setCategory("Gaming");
        request.setStatus(GiftStatus.AVAILABLE);
        request.setVisible(true);

        GiftResponse response = GiftResponse.builder()
                .id(UUID.randomUUID())
                .name("Test Gift")
                .category("Gaming")
                .status(GiftStatus.AVAILABLE)
                .visible(true)
                .build();

        when(giftService.create(any(GiftRequest.class))).thenAnswer(invocation -> {
            GiftRequest receivedRequest = invocation.getArgument(0);
            System.out.println("MOCK SERVICE RECEIVED: " + receivedRequest.getCategory());
            if (!"Gaming".equals(receivedRequest.getCategory())) {
                throw new RuntimeException(
                        "Category was not passed correctly! Received: " + receivedRequest.getCategory());
            }
            return response;
        });

        // Act & Assert
        mockMvc.perform(post("/api/v1/gifts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
