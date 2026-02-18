package com.projeto.dto;

import com.projeto.model.InvitationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CreateInvitationRequest {

    @NotBlank
    String familyName;

    @NotNull
    InvitationType type;

    String messageBody;

    String coverImageUrl;

    @Valid
    List<CreateGuestRequest> guests;
}