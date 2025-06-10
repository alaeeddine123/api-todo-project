package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.dto.CompanyBasicInfo;
import com.CHRESTAPI.todolist.dto.CompanyFinancialInfo;

/**
 * Service interface for external data integration
 */
public interface ExternalDataService {

    /**
     * Get basic company information from external APIs
     */
    CompanyBasicInfo getBasicCompanyInfo(String companyName);

    String generateSynergies(com.CHRESTAPI.todolist.entities.CompanyProfile company,
                             com.CHRESTAPI.todolist.entities.AnalysisRequest request);

    String generateIntegrationChallenges(com.CHRESTAPI.todolist.entities.CompanyProfile company);

    /**
     * Get financial information from external APIs
     */
    CompanyFinancialInfo getFinancialInfo(String companyName);

    /**
     * Get competitive analysis data
     */
    String getCompetitiveAnalysis(String companyName);

    /**
     * Get technology stack information
     */
    String getTechnologyInfo(String companyName);
}