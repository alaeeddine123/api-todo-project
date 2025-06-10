package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.RiskFactor;
import com.CHRESTAPI.todolist.enums.RiskCategory;

import java.util.List;

/**
 * Service interface for risk assessment
 */
public interface RiskAssessmentService {

    /**
     * Generate risk factors for a company profile
     */
    List<RiskFactor> generateRiskFactors(CompanyProfile companyProfile);

    /**
     * Assess specific risk category
     */
    List<RiskFactor> assessRiskCategory(CompanyProfile company, RiskCategory category);

    /**
     * Calculate overall risk score
     */
    Double calculateOverallRiskScore(CompanyProfile company);
}
