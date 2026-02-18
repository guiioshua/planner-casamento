package com.projeto.dto;

import com.projeto.model.GiftStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class GiftResponse {

    UUID id;
    String name;
    String purchaseLink;
    String imageUrl;
    GiftStatus status;
    boolean visible;
}