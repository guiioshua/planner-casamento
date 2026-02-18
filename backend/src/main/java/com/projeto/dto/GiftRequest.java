package com.projeto.dto;

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

    boolean active;
}

