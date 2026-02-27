package com.projeto.dto;

import com.projeto.model.GiftStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftRequest {

    @NotBlank
    private String name;

    private String purchaseLink;

    private String imageUrl;

    private Boolean visible;

    private String category;

    private GiftStatus status;
}