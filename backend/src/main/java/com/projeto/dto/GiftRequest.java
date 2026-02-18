package com.projeto.dto;

import com.projeto.model.GiftStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftRequest {

    @NotBlank
    String name;

    String purchaseLink;

    String imageUrl;

    @NotNull
    GiftStatus status;

    boolean visible;
}