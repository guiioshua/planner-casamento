package com.projeto.dto;

import com.projeto.model.GuestStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsvpUpdateRequest {

    /**
     * Map de convidado -> novo status
     */
    @NotEmpty
    Map<@NotNull UUID, @NotNull GuestStatus> statuses;
}
