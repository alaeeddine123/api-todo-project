package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for analysis result summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResultSummaryDto {

    private Long id;
    private Double overallScore; // 0-100
    private String recommendation; // STRONG_BUY, BUY, HOLD, AVOID

    // Key scores
    private Double strategicFitScore;
    private Double financialHealthScore;
    private Double marketOpportunityScore;

    // AI insights (abbreviated)
    private String executiveSummary; // First 200 chars
    private String topRisk; // Most critical risk
    private String topSynergy; // Best synergy opportunity

    // Analysis metadata
    private Double aiConfidenceScore;
    private LocalDateTime createdAt;
    private Integer processingTimeSeconds;
}
