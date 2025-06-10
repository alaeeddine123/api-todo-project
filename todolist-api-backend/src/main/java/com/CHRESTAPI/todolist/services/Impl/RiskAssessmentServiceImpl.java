package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.RiskFactor;
import com.CHRESTAPI.todolist.enums.RiskCategory;
import com.CHRESTAPI.todolist.enums.SeverityLevel;
import com.CHRESTAPI.todolist.repositories.RiskFactorRepository;
import com.CHRESTAPI.todolist.services.RiskAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final RiskFactorRepository riskFactorRepository;

    @Override
    public List<RiskFactor> generateRiskFactors(CompanyProfile companyProfile) {
        log.info("Generating risk factors for: {}", companyProfile.getCompanyName());

        List<RiskFactor> riskFactors = new ArrayList<>();

        try {
            // Generate different categories of risks
            riskFactors.addAll(generateFinancialRisks(companyProfile));
            riskFactors.addAll(generateOperationalRisks(companyProfile));
            riskFactors.addAll(generateRegulatoryRisks(companyProfile));
            riskFactors.addAll(generateTechnologyRisks(companyProfile));
            riskFactors.addAll(generateMarketRisks(companyProfile));

            // Save all risk factors
            return riskFactorRepository.saveAll(riskFactors);

        } catch (Exception e) {
            log.error("Error generating risk factors for: {}", companyProfile.getCompanyName(), e);
            return riskFactors;
        }
    }

    @Override
    public List<RiskFactor> assessRiskCategory(CompanyProfile company, RiskCategory category) {
        log.info("Assessing {} risks for: {}", category, company.getCompanyName());

        switch (category) {
            case FINANCIAL:
                return generateFinancialRisks(company);
            case OPERATIONAL:
                return generateOperationalRisks(company);
            case REGULATORY:
                return generateRegulatoryRisks(company);
            case TECHNOLOGY:
                return generateTechnologyRisks(company);
            case MARKET:
                return generateMarketRisks(company);
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public Double calculateOverallRiskScore(CompanyProfile company) {
        List<RiskFactor> allRisks = riskFactorRepository.findByCompanyProfile(company);

        if (allRisks.isEmpty()) {
            return 50.0; // Default moderate risk
        }

        double totalRiskScore = allRisks.stream().mapToDouble(risk -> risk.getImpactScore() * risk.getLikelihoodScore()).average().orElse(25.0);

        // Convert to 0-100 scale (lower is better)
        return Math.max(0, Math.min(100, totalRiskScore));
    }

    // Risk generation methods
    private List<RiskFactor> generateFinancialRisks(CompanyProfile company) {
        List<RiskFactor> risks = new ArrayList<>();

        // Cash flow risk
        if (company.getMonthlyBurnRate() != null && company.getAnnualRevenue() != null) {
            RiskFactor cashFlowRisk = createRiskFactor(company, RiskCategory.FINANCIAL, "Cash Flow Sustainability", "Monthly burn rate may exceed revenue generation capacity", SeverityLevel.MEDIUM, 6, 5);
            risks.add(cashFlowRisk);
        }

        // Valuation risk
        RiskFactor valuationRisk = createRiskFactor(company, RiskCategory.FINANCIAL, "Valuation Premium", "Current valuation may require premium acquisition pricing", SeverityLevel.MEDIUM, 5, 7);
        risks.add(valuationRisk);

        return risks;
    }

    private List<RiskFactor> generateOperationalRisks(CompanyProfile company) {
        List<RiskFactor> risks = new ArrayList<>();

        // Integration complexity
        RiskFactor integrationRisk = createRiskFactor(company, RiskCategory.OPERATIONAL, "Integration Complexity", "Operational integration may require significant resources and time", SeverityLevel.HIGH, 7, 6);
        risks.add(integrationRisk);

        return risks;
    }
    // Add these methods to your existing RiskAssessmentServiceImpl class:

    private List<RiskFactor> generateRegulatoryRisks(CompanyProfile company) {
        List<RiskFactor> risks = new ArrayList<>();

        // For now, return empty list - you can add risks manually later
        // Uncomment below if you want some basic regulatory risks:
    /*
    RiskFactor complianceRisk = createRiskFactor(
        company,
        RiskCategory.REGULATORY,
        "Insurance Regulatory Compliance",
        "Company may need to meet additional regulatory requirements",
        SeverityLevel.MEDIUM,
        5, 6
    );
    risks.add(complianceRisk);
    */

        return risks;
    }

    private List<RiskFactor> generateTechnologyRisks(CompanyProfile company) {
        List<RiskFactor> risks = new ArrayList<>();

        // For now, return empty list - you can add risks manually later
        // Uncomment below if you want some basic technology risks:
    /*
    RiskFactor techRisk = createRiskFactor(
        company,
        RiskCategory.TECHNOLOGY,
        "Technology Integration",
        "Integration with existing systems may be complex",
        SeverityLevel.MEDIUM,
        4, 5
    );
    risks.add(techRisk);
    */

        return risks;
    }

    private List<RiskFactor> generateMarketRisks(CompanyProfile company) {
        List<RiskFactor> risks = new ArrayList<>();

        // For now, return empty list - you can add risks manually later
        // Uncomment below if you want some basic market risks:
    /*
    RiskFactor marketRisk = createRiskFactor(
        company,
        RiskCategory.MARKET,
        "Market Competition",
        "Competitive pressures in target market",
        SeverityLevel.MEDIUM,
        4, 7
    );
    risks.add(marketRisk);
    */

        return risks;
    }

    // Also add the createRiskFactor method:
    private RiskFactor createRiskFactor(CompanyProfile companyProfile, RiskCategory category, String title, String description, SeverityLevel severity, Integer impactScore, Integer likelihoodScore) {

        List<Integer> numbers  = new ArrayList<>();
        numbers.stream().filter(c -> c % 2 == 0).map( c -> c * 2).toList();

        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setCompanyProfile(companyProfile);
        riskFactor.setRiskCategory(category);
        riskFactor.setRiskTitle(title);
        riskFactor.setRiskDescription(description);
        riskFactor.setSeverityLevel(severity);
        riskFactor.setImpactScore(impactScore);
        riskFactor.setLikelihoodScore(likelihoodScore);
        riskFactor.setMitigationStrategies(""); // Empty for now - you can add manually

        return riskFactor;
    }
}