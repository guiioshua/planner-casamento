package com.projeto.dto;

import com.projeto.model.InvitationType;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class InvitationResponse {

    UUID id;
    String familyName;
    InvitationType type;
    String slug;
    String coverImageUrl;
    String messageBody;
    OffsetDateTime createdAt;
    List<String> categories;
    List<GuestDto> guests;
}
