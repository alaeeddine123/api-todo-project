package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.dto.CompanyBasicInfo;
import com.CHRESTAPI.todolist.dto.CompanyFinancialInfo;
import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.CompanyProfile;

/**
 * Service interface for company data gathering and management
 */
public interface CompanyDataService {

    /**
     * Gather comprehensive company data from multiple sources
     */
    CompanyProfile gatherCompanyData(AnalysisRequest analysisRequest);

    /**
     * Check if company data needs refresh
     */
    boolean needsDataRefresh(CompanyProfile profile);

    CompanyBasicInfo getBasicCompanyInfo(String companyName);

    CompanyFinancialInfo getFinancialInfo(String companyName);

    String getCompetitiveAnalysis(String companyName);

    String getTechnologyInfo(String companyName);

    /**
     * Refresh company data from external sources
     */
    CompanyProfile refreshCompanyData(CompanyProfile profile);
}