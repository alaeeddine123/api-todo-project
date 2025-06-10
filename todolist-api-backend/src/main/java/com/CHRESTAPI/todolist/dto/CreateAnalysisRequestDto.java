package com.CHRESTAPI.todolist.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new analysis request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAnalysisRequestDto {

    @NotBlank(message = "Target company name is required")
    @Size(min = 2, max = 255, message = "Company name must be between 2 and 255 characters")
    private String targetCompanyName;

    @Size(max = 255, message = "Company domain must not exceed 255 characters")
    private String targetCompanyDomain; // Optional: company website

    @Size(max = 500, message = "Acquisition purpose must not exceed 500 characters")
    private String acquisitionPurpose; // e.g., "expand insurtech capabilities"

    @DecimalMin(value = "0.0", inclusive = false, message = "Budget range must be positive")
    private BigDecimal budgetRange;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes; // User's additional context
}