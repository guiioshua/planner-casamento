package com.projeto.dto;

import com.projeto.model.GiftStatus;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftRequest {

    @NotBlank
    String name;

    String purchaseLink;

    String imageUrl;

    Boolean visible;

    GiftStatus status;
}