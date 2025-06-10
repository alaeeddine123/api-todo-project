package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for detailed analysis result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultDetailDto {

    private Long id;

    // Overall metrics
    private Double overallScore; // 0-100
    private String recommendation; // STRONG_BUY, BUY, HOLD, AVOID

    // Detailed scores
    private Double strategicFitScore; // 0-100
    private Double financialHealthScore; // 0-100
    private Double marketOpportunityScore; // 0-100
    private Double integrationRiskScore; // 0-100 (higher = more risky)
    private Double regulatoryComplianceScore; // 0-100
    private Double technologyAlignmentScore; // 0-100

    // AI-generated insights
    private String executiveSummary;
    private String keyStrengths;
    private String primaryRisks;
    private String synergyOpportunities;
    private String integrationChallenges;

    // Valuation estimates
    private BigDecimal estimatedFairValue;
    private String valuationMethodology;
    private String comparableCompanies; // JSON string

    // Analysis metadata
    private String analysisVersion;
    private Double aiConfidenceScore;
    private LocalDateTime createdAt;
    private Integer processingTimeSeconds;
}

