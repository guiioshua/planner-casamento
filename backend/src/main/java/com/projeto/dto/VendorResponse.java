package com.projeto.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class VendorResponse {

    UUID id;
    String companyName;
    String serviceCategory;
    String contactName;
    String contactPhone;
    BigDecimal price;
    BigDecimal amountPaid;
    String notes;
}

