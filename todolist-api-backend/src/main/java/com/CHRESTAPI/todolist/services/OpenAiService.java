package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.CompanyProfile;

/**
 * Service interface for AI/LLM integration
 */
public interface OpenAiService {

    /**
     * Get analysis score from AI
     */
    Double getAnalysisScore(String prompt, String analysisType);

    /**
     * Generate executive summary
     */
    String generateExecutiveSummary(CompanyProfile company, AnalysisResult result);

    /**
     * Generate key strengths analysis
     */
    String generateKeyStrengths(CompanyProfile company);

    /**
     * Generate primary risks analysis
     */
    String generatePrimaryRisks(CompanyProfile company);

    /**
     * Generate synergy opportunities
     */
    String generateSynergies(CompanyProfile company, AnalysisRequest request);

    /**
     * Generate integration challenges analysis
     */
    String generateIntegrationChallenges(CompanyProfile company);
}

