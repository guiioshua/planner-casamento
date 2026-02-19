package com.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateGuestRequest {

    @NotBlank
    String fullName;

    String phone;

    String status;
}
