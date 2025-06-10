package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.dto.CompanyBasicInfo;
import com.CHRESTAPI.todolist.dto.CompanyFinancialInfo;
import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.repositories.CompanyProfileRepository;
import com.CHRESTAPI.todolist.services.CompanyDataService;
import com.CHRESTAPI.todolist.services.ExternalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of CompanyDataService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyDataServiceImpl implements CompanyDataService {

    private final CompanyProfileRepository companyProfileRepository;
    private final ExternalDataService externalDataService;

    @Override
    public CompanyProfile gatherCompanyData(AnalysisRequest analysisRequest) {
        log.info("Gathering data for company: {}", analysisRequest.getTargetCompanyName());

        CompanyProfile profile = new CompanyProfile();
        profile.setAnalysisRequest(analysisRequest);
        profile.setCompanyName(analysisRequest.getTargetCompanyName());
        profile.setWebsite(analysisRequest.getTargetCompanyDomain());
        profile.setDataLastUpdated(LocalDateTime.now());

        try {
            // Gather data from external APIs
            enrichCompanyProfile(profile);

            CompanyProfile savedProfile = companyProfileRepository.save(profile);
            log.info("Company profile created for: {}", profile.getCompanyName());
            return savedProfile;

        } catch (Exception e) {
            log.error("Error gathering company data for: {}", analysisRequest.getTargetCompanyName(), e);
            throw new RuntimeException("Failed to gather company data", e);
        }
    }

    @Override
    public boolean needsDataRefresh(CompanyProfile profile) {
        if (profile.getDataLastUpdated() == null) {
            return true;
        }

        // Refresh data older than 7 days
        return profile.getDataLastUpdated().isBefore(LocalDateTime.now().minusDays(7));
    }


      @Override
    public CompanyBasicInfo getBasicCompanyInfo(String companyName) {
        log.info("Fetching basic company info for: {}", companyName);

        try {
            // TODO: Integrate with real APIs like Crunchbase, etc.
            // For now, return simulated data
            return CompanyBasicInfo.builder()
                .description(generateDescription(companyName))
                .industry(determineIndustry(companyName))
                .foundedYear(2018) // Default
                .employeeCount(50) // Default
                .location("San Francisco, CA") // Default
                .build();

        } catch (Exception e) {
            log.error("Error fetching basic company info for: {}", companyName, e);
            return null;
        }
    }

    @Override
    public CompanyFinancialInfo getFinancialInfo(String companyName) {
        log.info("Fetching financial info for: {}", companyName);

        try {
            // TODO: Integrate with financial data APIs
            // For now, return simulated data
            return CompanyFinancialInfo.builder()
                .totalFunding(new BigDecimal("5000000")) // $5M default
                .valuation(new BigDecimal("20000000")) // $20M default
                .revenue(new BigDecimal("1000000")) // $1M default
                .lastFundingRound("Series A")
                .build();

        } catch (Exception e) {
            log.error("Error fetching financial info for: {}", companyName, e);
            return null;
        }
    }

    @Override
    public String getCompetitiveAnalysis(String companyName) {
        log.info("Fetching competitive analysis for: {}", companyName);

        // TODO: Integrate with market research APIs
        return String.format("Competitive analysis for %s shows strong market positioning " +
            "with innovative technology solutions and growing customer base.", companyName);
    }

    @Override
    public String getTechnologyInfo(String companyName) {
        log.info("Fetching technology info for: {}", companyName);

        // TODO: Integrate with technology assessment APIs
        return "Modern technology stack including cloud infrastructure, " +
            "microservices architecture, and API-first design.";
    }

    // Helper methods for simulated data
    private String generateDescription(String companyName) {
        return String.format("%s is an innovative technology company focused on " +
            "digital transformation and cutting-edge solutions for the insurance industry.",
            companyName);
    }

    private String determineIndustry(String companyName) {
        // Simple logic - in real implementation, you'd use external APIs
        String lowerName = companyName.toLowerCase();
        if (lowerName.contains("insur") || lowerName.contains("tech")) {
            return "InsurTech";
        } else if (lowerName.contains("fin")) {
            return "FinTech";
        }
        return "Technology";
    }

// Note: You'll also need to move CompanyBasicInfo and CompanyFinancialInfo
// from the service layer to the dto package to match your imports

    @Override
    public CompanyProfile refreshCompanyData(CompanyProfile profile) {
        log.info("Refreshing data for company: {}", profile.getCompanyName());

        enrichCompanyProfile(profile);
        profile.setDataLastUpdated(LocalDateTime.now());

        return companyProfileRepository.save(profile);
    }

    /**
     * Enrich company profile with external data
     */
    private void enrichCompanyProfile(CompanyProfile profile) {
        // Basic company information
        CompanyBasicInfo basicInfo = externalDataService.getBasicCompanyInfo(profile.getCompanyName());
        if (basicInfo != null) {
            profile.setDescription(basicInfo.getDescription());
            profile.setIndustry(basicInfo.getIndustry());
            profile.setFoundedYear(basicInfo.getFoundedYear());
            profile.setEmployeeCount(basicInfo.getEmployeeCount());
            profile.setHeadquartersLocation(basicInfo.getLocation());
        }

        // Financial information
        CompanyFinancialInfo financialInfo = externalDataService.getFinancialInfo(profile.getCompanyName());
        if (financialInfo != null) {
            profile.setTotalFunding(financialInfo.getTotalFunding());
            profile.setLastValuation(financialInfo.getValuation());
            profile.setAnnualRevenue(financialInfo.getRevenue());
            profile.setLastFundingRound(financialInfo.getLastFundingRound());
        }

        // Market and technology information
        profile.setCompetitiveAdvantages(externalDataService.getCompetitiveAnalysis(profile.getCompanyName()));
        profile.setTechnologyStack(externalDataService.getTechnologyInfo(profile.getCompanyName()));

        // Set data sources for transparency
        profile.setDataSources("Crunchbase API, Alpha Vantage, Company Website, News APIs");
    }
}