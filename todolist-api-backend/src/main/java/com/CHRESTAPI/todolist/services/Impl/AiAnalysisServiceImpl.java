package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.enums.AcquisitionRecommendation;
import com.CHRESTAPI.todolist.enums.RequestStatus;
import com.CHRESTAPI.todolist.repositories.AnalysisRequestRepository;
import com.CHRESTAPI.todolist.repositories.AnalysisResultRepository;
import com.CHRESTAPI.todolist.services.AiAnalysisService;
import com.CHRESTAPI.todolist.services.CompanyDataService;
import com.CHRESTAPI.todolist.services.OpenAiService;
import com.CHRESTAPI.todolist.services.RiskAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of AiAnalysisService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiAnalysisServiceImpl implements AiAnalysisService {

    private final AnalysisResultRepository analysisResultRepository;
    private final AnalysisRequestRepository analysisRequestRepository;
    private final CompanyDataService companyDataService;
    private final OpenAiService openAiService;
    private final RiskAssessmentService riskAssessmentService;

    @Override
    @Transactional
    public void startAnalysisAsync(AnalysisRequest request) {
        log.info("Starting AI analysis for request: {}", request.getId());

        try {
            // Update status to processing
            request.setStatus(RequestStatus.PROCESSING);
            analysisRequestRepository.save(request);

            // Step 1: Gather company data
            CompanyProfile companyProfile = companyDataService.gatherCompanyData(request);

            // Step 2: Perform AI analysis
            AnalysisResult result = performAnalysis(request, companyProfile);

            // Step 3: Generate risk assessment
            riskAssessmentService.generateRiskFactors(companyProfile);

            // Step 4: Complete the analysis
            request.setStatus(RequestStatus.COMPLETED);
            request.setCompletedAt(LocalDateTime.now());
            analysisRequestRepository.save(request);

            log.info("AI analysis completed for request: {}", request.getId());

        } catch (Exception e) {
            log.error("AI analysis failed for request: {}", request.getId(), e);
            request.setStatus(RequestStatus.FAILED);
            request.setCompletedAt(LocalDateTime.now());
            analysisRequestRepository.save(request);
        }
    }

    @Override
    public AnalysisResult performAnalysis(AnalysisRequest request, CompanyProfile companyProfile) {
        log.info("Performing AI analysis for company: {}", companyProfile.getCompanyName());

        LocalDateTime startTime = LocalDateTime.now();

        AnalysisResult result = new AnalysisResult();
        result.setRequest(request);
        result.setCreatedAt(LocalDateTime.now());

        // Calculate individual scores
        result.setStrategicFitScore(calculateStrategicFitScore(companyProfile, request));
        result.setFinancialHealthScore(calculateFinancialHealthScore(companyProfile));
        result.setMarketOpportunityScore(calculateMarketOpportunityScore(companyProfile));
        result.setIntegrationRiskScore(calculateIntegrationRiskScore(companyProfile));
        result.setRegulatoryComplianceScore(calculateRegulatoryScore(companyProfile));
        result.setTechnologyAlignmentScore(calculateTechnologyScore(companyProfile));

        // Calculate overall score (weighted average)
        result.setOverallScore(calculateOverallScore(result));

        // Generate recommendation
        result.setRecommendation(generateRecommendation(result.getOverallScore()));

        // Generate AI insights
        generateAiInsights(result, companyProfile, request);

        // Calculate processing time
        long processingSeconds = java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
        result.setProcessingTimeSeconds((int) processingSeconds);

        return analysisResultRepository.save(result);
    }

    @Override
    public AnalysisResult recalculateAnalysis(AnalysisRequest request) {
        CompanyProfile companyProfile = request.getCompanyProfile();
        if (companyProfile == null) {
            throw new IllegalArgumentException("No company profile found for analysis request");
        }

        return performAnalysis(request, companyProfile);
    }

    // Private helper methods for scoring algorithms
    private Double calculateStrategicFitScore(CompanyProfile company, AnalysisRequest request) {
        String prompt = String.format(
            "Analyze strategic fit for acquisition. Target: %s in %s industry. Purpose: %s. " +
            "Company description: %s. Provide score 0-100.",
            company.getCompanyName(), company.getIndustry(),
            request.getAcquisitionPurpose(), company.getDescription()
        );

        return openAiService.getAnalysisScore(prompt, "strategic_fit");
    }

    private Double calculateFinancialHealthScore(CompanyProfile company) {
        double score = 50.0; // Base score

        if (company.getAnnualRevenue() != null && company.getAnnualRevenue().doubleValue() > 0) {
            score += 20; // Has revenue
        }

        if (company.getTotalFunding() != null) {
            score += 15; // Well funded
        }

        if (company.getEmployeeCount() != null && company.getEmployeeCount() > 10) {
            score += 10; // Established team
        }

        return Math.min(score, 100.0);
    }

    private Double calculateMarketOpportunityScore(CompanyProfile company) {
        return openAiService.getAnalysisScore(
            String.format("Analyze market opportunity for %s in %s industry. Description: %s",
                         company.getCompanyName(), company.getIndustry(), company.getDescription()),
            "market_opportunity"
        );
    }

    private Double calculateIntegrationRiskScore(CompanyProfile company) {
        double baseScore = 70.0;

        if (company.getEmployeeCount() != null && company.getEmployeeCount() > 500) {
            baseScore -= 20; // Large companies harder to integrate
        }

        if (company.getTechnologyStack() != null && company.getTechnologyStack().contains("legacy")) {
            baseScore -= 15; // Legacy tech harder to integrate
        }

        return Math.max(baseScore, 0.0);
    }

    private Double calculateRegulatoryScore(CompanyProfile company) {
        if ("fintech".equalsIgnoreCase(company.getIndustry()) ||
            "insurtech".equalsIgnoreCase(company.getIndustry())) {
            return 80.0; // Good fit for insurance
        }
        return 60.0; // Default score
    }

    private Double calculateTechnologyScore(CompanyProfile company) {
        return openAiService.getAnalysisScore(
            String.format("Assess technology alignment for insurance company acquiring %s. Tech stack: %s",
                         company.getCompanyName(), company.getTechnologyStack()),
            "technology_alignment"
        );
    }

    private Double calculateOverallScore(AnalysisResult result) {
        return (result.getStrategicFitScore() * 0.25 +
                result.getFinancialHealthScore() * 0.20 +
                result.getMarketOpportunityScore() * 0.20 +
                result.getIntegrationRiskScore() * 0.15 +
                result.getRegulatoryComplianceScore() * 0.10 +
                result.getTechnologyAlignmentScore() * 0.10);
    }

    private AcquisitionRecommendation generateRecommendation(Double overallScore) {
        if (overallScore >= 80) return AcquisitionRecommendation.STRONG_BUY;
        if (overallScore >= 65) return AcquisitionRecommendation.BUY;
        if (overallScore >= 40) return AcquisitionRecommendation.HOLD;
        return AcquisitionRecommendation.AVOID;
    }

    private void generateAiInsights(AnalysisResult result, CompanyProfile company, AnalysisRequest request) {
        result.setExecutiveSummary(openAiService.generateExecutiveSummary(company, result));
        result.setKeyStrengths(openAiService.generateKeyStrengths(company));
        result.setPrimaryRisks(openAiService.generatePrimaryRisks(company));
        result.setSynergyOpportunities(openAiService.generateSynergies(company, request));
        result.setIntegrationChallenges(openAiService.generateIntegrationChallenges(company));
        result.setAiConfidenceScore(85.0); // AI confidence in the analysis
    }
}