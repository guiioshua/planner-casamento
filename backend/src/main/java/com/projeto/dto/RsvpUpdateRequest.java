package com.projeto.dto;

import com.projeto.model.GuestStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class RsvpUpdateRequest {

    /**
     * Map de convidado -> novo status
     */
    @NotEmpty
    Map<@NotNull UUID, @NotNull GuestStatus> statuses;
}

