package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileDetailDto {

    private Long id;
    private String companyName;
    private String website;
    private String description;
    private String industry;
    private String subIndustry;
    private Integer foundedYear;
    private Integer employeeCount;
    private String headquartersLocation;
    private String companyStage;

    // Financial details
    private BigDecimal totalFunding;
    private BigDecimal lastValuation;
    private BigDecimal annualRevenue;
    private BigDecimal monthlyBurnRate;
    private String lastFundingRound;

    // Product & Technology
    private String primaryProduct;
    private String technologyStack;
    private String competitiveAdvantages;

    // Market position
    private String targetMarket;
    private Integer customerBaseSize;
    private Double marketSharePercentage;

    // Data metadata
    private String dataSources;
    private LocalDateTime dataLastUpdated;
    private Boolean needsRefresh;
}