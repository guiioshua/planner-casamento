package com.projeto.dto;

import com.projeto.model.InvitationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvitationRequest {

    @NotBlank
    String familyName;

    @NotNull
    InvitationType type;

    String messageBody;

    String coverImageUrl;

    List<String> categories;

    @Valid
    List<CreateGuestRequest> guests;
}