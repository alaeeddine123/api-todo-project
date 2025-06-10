package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for company profile summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileSummaryDto {

    private Long id;
    private String companyName;
    private String website;
    private String description;
    private String industry;
    private String subIndustry;
    private Integer foundedYear;
    private Integer employeeCount;
    private String headquartersLocation;
    private String companyStage; // SEED, SERIES_A, etc.

    // Financial summary
    private BigDecimal totalFunding;
    private BigDecimal lastValuation;
    private BigDecimal annualRevenue;
    private String lastFundingRound;

    // Data freshness
    private LocalDateTime dataLastUpdated;
    private Boolean dataIsStale; // If data needs refresh
}
