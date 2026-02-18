package com.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class VendorRequest {

    @NotBlank
    String companyName;

    @NotBlank
    String serviceCategory;

    String contactName;

    String contactPhone;

    @NotNull
    BigDecimal price;

    BigDecimal amountPaid;

    String notes;
}

