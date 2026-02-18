package com.projeto.dto;

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
    boolean active;
}

