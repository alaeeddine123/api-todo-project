package com.CHRESTAPI.todolist.entities;

import com.CHRESTAPI.todolist.enums.AcquisitionRecommendation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ====================== ANALYSIS RESULT ======================
@Entity
@Table(name = "analysis_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "analysis_request_id")
    private AnalysisRequest request;

    // Overall Metrics
    @Column(name = "overall_score", nullable = false)
    private Double overallScore; // 0-100

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private AcquisitionRecommendation recommendation;

    // Detailed Scores
    @Column(name = "strategic_fit_score")
    private Double strategicFitScore; // 0-100

    @Column(name = "financial_health_score")
    private Double financialHealthScore; // 0-100

    @Column(name = "market_opportunity_score")
    private Double marketOpportunityScore; // 0-100

    @Column(name = "integration_risk_score")
    private Double integrationRiskScore; // 0-100 (higher = more risky)

    @Column(name = "regulatory_compliance_score")
    private Double regulatoryComplianceScore; // 0-100

    @Column(name = "technology_alignment_score")
    private Double technologyAlignmentScore; // 0-100

    // AI-Generated Insights
    @Column(name = "executive_summary", length = 3000)
    private String executiveSummary;

    @Column(name = "key_strengths", length = 2000)
    private String keyStrengths;

    @Column(name = "primary_risks", length = 2000)
    private String primaryRisks;

    @Column(name = "synergy_opportunities", length = 2000)
    private String synergyOpportunities;

    @Column(name = "integration_challenges", length = 2000)
    private String integrationChallenges;

    // Valuation Estimates
    @Column(name = "estimated_fair_value", precision = 15, scale = 2)
    private BigDecimal estimatedFairValue;

    @Column(name = "valuation_methodology")
    private String valuationMethodology;

    @Column(name = "comparable_companies", length = 1000)
    private String comparableCompanies; // JSON string

    // Analysis Metadata
    @Column(name = "analysis_version")
    private String analysisVersion = "1.0";

    @Column(name = "ai_confidence_score")
    private Double aiConfidenceScore; // How confident the AI is in its analysis

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "processing_time_seconds")
    private Integer processingTimeSeconds;
}

