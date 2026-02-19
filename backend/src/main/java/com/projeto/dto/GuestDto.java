package com.projeto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projeto.model.GuestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class GuestDto {

    UUID id;

    @NotBlank
    String fullName;

    String phone;

    @NotNull
    GuestStatus status;

    @JsonProperty("isChild")
    boolean isChild;
}
