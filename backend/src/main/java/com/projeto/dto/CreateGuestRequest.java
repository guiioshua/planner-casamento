package com.projeto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGuestRequest {

    @NotBlank
    String fullName;

    String phone;

    String status;

    @JsonProperty("isChild")
    boolean isChild;
}
