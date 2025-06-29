package com.CHRESTAPI.todolist.llm;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.CompanyProfile;

/**
 * Service interface for LLM-powered analysis operations
 * Supports multiple LLM providers (Claude, OpenAI, etc.)
 */
public interface LlmService {

    /**
     * Generate an analysis score for a specific metric
     * @param prompt The analysis prompt
     * @param analysisType The type of analysis (strategic_fit, financial_health, etc.)
     * @return Score from 0-100
     */
    Double getAnalysisScore(String prompt, String analysisType);

    /**
     * Generate executive summary for acquisition analysis
     * @param company The target company profile
     * @param result The analysis results
     * @return Professional executive summary
     */
    String generateExecutiveSummary(CompanyProfile company, AnalysisResult result);

    /**
     * Identify key strengths of the target company
     * @param company The company profile
     * @return Formatted list of key strengths
     */
    String generateKeyStrengths(CompanyProfile company);

    /**
     * Assess primary risks of the acquisition
     * @param company The company profile
     * @return Formatted list of primary risks
     */
    String generatePrimaryRisks(CompanyProfile company);

    /**
     * Identify potential synergies from the acquisition
     * @param company The company profile
     * @param request The analysis request context
     * @return Formatted list of synergy opportunities
     */
    String generateSynergies(CompanyProfile company, AnalysisRequest request);

    /**
     * Assess integration challenges
     * @param company The company profile
     * @return Formatted list of integration challenges
     */
    String generateIntegrationChallenges(CompanyProfile company);
}