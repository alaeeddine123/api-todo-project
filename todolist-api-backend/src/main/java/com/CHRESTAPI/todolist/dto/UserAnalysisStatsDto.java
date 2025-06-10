package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user analysis statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalysisStatsDto {

    private long totalAnalyses;
    private long completedAnalyses;
    private long pendingAnalyses;
    private long failedAnalyses;

    // Score statistics (for completed analyses)
    private Double averageScore;
    private Double highestScore;
    private String bestRecommendedCompany;

    // Time statistics
    private Double averageProcessingTimeMinutes;
    private LocalDateTime lastAnalysisDate;
    private Integer analysesThisMonth;

    // Industry breakdown
    private java.util.Map<String, Long> analysesByIndustry;
    private java.util.Map<String, Long> analysesByRecommendation;
}

