package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for dashboard overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewDto {

    // User stats
    private UserAnalysisStatsDto userStats;

    // Recent activity
    private java.util.List<AnalysisRequestSummaryDto> recentAnalyses;
    private java.util.List<AnalysisRequestSummaryDto> topRecommendations;

    // Pending items
    private java.util.List<AnalysisRequestSummaryDto> pendingAnalyses;
    private Integer pendingCount;

    // Insights
    private String monthlyTrend; // "up", "down", "stable"
    private java.util.List<String> suggestedIndustries; // Based on user's history
    private java.util.List<String> trendingCompanies; // Popular analysis targets
}