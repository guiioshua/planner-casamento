package com.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
