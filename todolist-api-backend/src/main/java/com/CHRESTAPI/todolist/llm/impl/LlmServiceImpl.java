package com.CHRESTAPI.todolist.llm.impl;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.llm.ClaudeMessage;
import com.CHRESTAPI.todolist.llm.ClaudeRequest;
import com.CHRESTAPI.todolist.llm.ClaudeResponse;
import com.CHRESTAPI.todolist.llm.LlmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmServiceImpl implements LlmService {

    @Value("${claude.api.key}")
    private String claudeApiKey;

    @Value("${claude.api.url:https://api.anthropic.com/v1/messages}")
    private String claudeApiUrl;

    @Value("${claude.model:claude-sonnet-4-20250514}")
    private String claudeModel;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Double getAnalysisScore(String prompt, String analysisType) {
        log.info("Generating Claude analysis score for type: {}", analysisType);

        try {
            String enhancedPrompt = buildScoringPrompt(prompt, analysisType);
            String response = callClaudeApi(enhancedPrompt);
            
            return extractScoreFromResponse(response);

        } catch (Exception e) {
            log.error("Error generating Claude analysis score for type: {}", analysisType, e);
            return getDefaultScore(analysisType);
        }
    }

    @Override
    public String generateExecutiveSummary(CompanyProfile company, AnalysisResult result) {
        log.info("Generating executive summary for: {}", company.getCompanyName());

        try {
            String prompt = String.format("""
                As a senior M&A advisor, create a concise executive summary for the acquisition analysis of %s.
                
                Company Details:
                - Name: %s
                - Industry: %s
                - Description: %s
                - Employee Count: %s
                - Annual Revenue: %s
                - Funding: %s
                
                Analysis Scores:
                - Overall Score: %.1f/100
                - Strategic Fit: %.1f/100
                - Financial Health: %.1f/100
                - Market Opportunity: %.1f/100
                - Integration Risk: %.1f/100 (lower is better)
                - Regulatory Compliance: %.1f/100
                - Technology Alignment: %.1f/100
                
                Recommendation: %s
                
                Provide a 3-4 sentence executive summary that covers:
                1. Overall acquisition attractiveness
                2. Key value drivers
                3. Primary considerations
                4. Strategic rationale
                
                Keep it professional and actionable for C-level executives.
                """,
                company.getCompanyName(),
                company.getCompanyName(),
                company.getIndustry(),
                company.getDescription(),
                company.getEmployeeCount(),
                company.getAnnualRevenue(),
                company.getTotalFunding(),
                result.getOverallScore(),
                result.getStrategicFitScore(),
                result.getFinancialHealthScore(),
                result.getMarketOpportunityScore(),
                result.getIntegrationRiskScore(),
                result.getRegulatoryComplianceScore(),
                result.getTechnologyAlignmentScore(),
                result.getRecommendation()
            );

            return callClaudeApi(prompt);

        } catch (Exception e) {
            log.error("Error generating executive summary for: {}", company.getCompanyName(), e);
            return generateFallbackExecutiveSummary(company, result);
        }
    }

    @Override
    public String generateKeyStrengths(CompanyProfile company) {
        log.info("Generating key strengths for: {}", company.getCompanyName());

        try {
            String prompt = String.format("""
                Analyze the key strengths of %s for potential acquisition.
                
                Company Information:
                - Industry: %s
                - Description: %s
                - Founded: %s
                - Employees: %s
                - Revenue: %s
                - Funding: %s
                - Technology: %s
                - Competitive Advantages: %s
                
                Identify 4-5 key strengths that make this company attractive for acquisition.
                Focus on:
                - Market position and competitive advantages
                - Technology and innovation capabilities
                - Financial performance and growth
                - Team and organizational strengths
                - Strategic value for acquirer
                
                Format as bullet points. Be specific and quantify where possible.
                """,
                company.getCompanyName(),
                company.getIndustry(),
                company.getDescription(),
                company.getFoundedYear(),
                company.getEmployeeCount(),
                company.getAnnualRevenue(),
                company.getTotalFunding(),
                company.getTechnologyStack(),
                company.getCompetitiveAdvantages()
            );

            return callClaudeApi(prompt);

        } catch (Exception e) {
            log.error("Error generating key strengths for: {}", company.getCompanyName(), e);
            return generateFallbackStrengths(company);
        }
    }

    @Override
    public String generatePrimaryRisks(CompanyProfile company) {
        log.info("Generating primary risks for: {}", company.getCompanyName());

        try {
            String prompt = String.format("""
                Conduct a comprehensive risk assessment for acquiring %s.
                
                Company Details:
                - Industry: %s
                - Stage: %s
                - Employee Count: %s
                - Revenue: %s
                - Burn Rate: %s
                - Technology Stack: %s
                - Market Position: %s
                
                Identify 4-6 primary risks across these categories:
                - Financial risks (cash flow, valuation, profitability)
                - Operational risks (integration, key personnel, systems)
                - Market risks (competition, customer concentration, market changes)
                - Regulatory/compliance risks
                - Technology risks (legacy systems, security, scalability)
                
                For each risk, briefly explain the potential impact.
                Format as bullet points with risk category labels.
                """,
                company.getCompanyName(),
                company.getIndustry(),
                company.getCompanyStage(),
                company.getEmployeeCount(),
                company.getAnnualRevenue(),
                company.getMonthlyBurnRate(),
                company.getTechnologyStack(),
                company.getCompetitiveAdvantages()
            );

            return callClaudeApi(prompt);

        } catch (Exception e) {
            log.error("Error generating primary risks for: {}", company.getCompanyName(), e);
            return generateFallbackRisks(company);
        }
    }

    @Override
    public String generateSynergies(CompanyProfile company, AnalysisRequest request) {
        log.info("Generating synergies for: {}", company.getCompanyName());

        try {
            String prompt = String.format("""
                Identify potential synergies from acquiring %s.
                
                Target Company:
                - Name: %s
                - Industry: %s
                - Description: %s
                - Technology: %s
                - Market: %s
                - Customer Base: %s
                
                Acquisition Context:
                - Purpose: %s
                - Budget Range: %s
                
                Identify 4-6 specific synergy opportunities:
                - Revenue synergies (cross-selling, market expansion, new products)
                - Cost synergies (operational efficiency, technology, shared services)
                - Strategic synergies (capabilities, market position, innovation)
                - Technology synergies (platform integration, data, automation)
                
                For each synergy, estimate the potential value or impact.
                Format as bullet points with synergy type labels.
                Be specific and actionable.
                """,
                company.getCompanyName(),
                company.getCompanyName(),
                company.getIndustry(),
                company.getDescription(),
                company.getTechnologyStack(),
                company.getTargetMarket(),
                company.getCustomerBaseSize(),
                request.getAcquisitionPurpose(),
                request.getBudgetRange()
            );

            return callClaudeApi(prompt);

        } catch (Exception e) {
            log.error("Error generating synergies for: {}", company.getCompanyName(), e);
            return generateFallbackSynergies(company, request);
        }
    }

    @Override
    public String generateIntegrationChallenges(CompanyProfile company) {
        log.info("Generating integration challenges for: {}", company.getCompanyName());

        try {
            String prompt = String.format("""
                Assess integration challenges for acquiring %s.
                
                Company Profile:
                - Industry: %s
                - Size: %s employees
                - Technology Stack: %s
                - Stage: %s
                - Location: %s
                - Culture/Background: %s
                
                Identify 4-6 key integration challenges:
                - Cultural integration and change management
                - Technology and systems integration
                - Operational integration (processes, procedures)
                - Personnel retention and management
                - Customer and vendor relationship management
                - Regulatory and compliance alignment
                
                For each challenge, suggest potential mitigation strategies.
                Format as bullet points with challenge categories.
                Focus on practical, actionable insights.
                """,
                company.getCompanyName(),
                company.getIndustry(),
                company.getEmployeeCount(),
                company.getTechnologyStack(),
                company.getCompanyStage(),
                company.getHeadquartersLocation(),
                company.getDescription()
            );

            return callClaudeApi(prompt);

        } catch (Exception e) {
            log.error("Error generating integration challenges for: {}", company.getCompanyName(), e);
            return generateFallbackIntegrationChallenges(company);
        }
    }

    // Private helper methods
    private String buildScoringPrompt(String basePrompt, String analysisType) {
        return String.format("""
            %s
            
            As an expert M&A analyst, provide a numerical score from 0-100 where:
            - 90-100: Exceptional/Outstanding
            - 80-89: Very Strong/Excellent
            - 70-79: Strong/Good
            - 60-69: Moderate/Average
            - 40-59: Weak/Below Average
            - 0-39: Very Poor/Critical Issues
            
            Consider industry standards and best practices for %s analysis.
            
            Respond with just the numerical score (e.g., "75") followed by a brief 1-sentence justification.
            """, basePrompt, analysisType);
    }

    private String callClaudeApi(String prompt) {
        try {
            ClaudeRequest request = new ClaudeRequest();
            request.setModel(claudeModel);
            request.setMaxTokens(1500);
            request.setMessages(List.of(new ClaudeMessage("user", prompt)));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", claudeApiKey);
            headers.set("anthropic-version", "2023-06-01");

            HttpEntity<ClaudeRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<ClaudeResponse> response = restTemplate.exchange(
                claudeApiUrl, 
                HttpMethod.POST, 
                entity, 
                ClaudeResponse.class
            );

            if (response.getBody() != null && !response.getBody().getContent().isEmpty()) {
                return response.getBody().getContent().get(0).getText();
            }

            throw new RuntimeException("Empty response from Claude API");

        } catch (Exception e) {
            log.error("Error calling Claude API", e);
            throw new RuntimeException("Claude API call failed", e);
        }
    }

    private Double extractScoreFromResponse(String response) {
        // Extract numerical score from Claude's response
        Pattern scorePattern = Pattern.compile("\\b(\\d{1,3})\\b");
        Matcher matcher = scorePattern.matcher(response);
        
        if (matcher.find()) {
            int score = Integer.parseInt(matcher.group(1));
            if (score >= 0 && score <= 100) {
                return (double) score;
            }
        }
        
        log.warn("Could not extract valid score from response: {}", response);
        return 65.0; // Default fallback
    }

    private Double getDefaultScore(String analysisType) {
        return switch (analysisType.toLowerCase()) {
            case "strategic_fit" -> 75.0;
            case "financial_health" -> 70.0;
            case "market_opportunity" -> 68.0;
            case "technology_alignment" -> 72.0;
            default -> 65.0;
        };
    }

    // Fallback methods for when API calls fail
    private String generateFallbackExecutiveSummary(CompanyProfile company, AnalysisResult result) {
        return String.format(
            "Analysis of %s reveals a %s acquisition opportunity with an overall score of %.1f. " +
            "The company demonstrates capabilities in the %s sector and shows potential for strategic value creation. " +
            "Key considerations include integration complexity and market positioning.",
            company.getCompanyName(),
            getRecommendationText(result.getRecommendation()),
            result.getOverallScore(),
            company.getIndustry()
        );
    }

    private String generateFallbackStrengths(CompanyProfile company) {
        return String.format("""
            • Established market presence in %s industry
            • Experienced team with %d+ professionals
            • Proven technology platform and infrastructure
            • Growing customer base and market traction
            • Strategic alignment with industry trends
            """,
            company.getIndustry(),
            company.getEmployeeCount() != null ? company.getEmployeeCount() : 50
        );
    }

    private String generateFallbackRisks(CompanyProfile company) {
        return String.format("""
            • Integration complexity with existing systems and processes
            • Regulatory compliance requirements in %s sector
            • Key personnel retention during transition period
            • Technology modernization and platform integration needs
            • Market competition and customer retention challenges
            """,
            company.getIndustry()
        );
    }

    private String generateFallbackSynergies(CompanyProfile company, AnalysisRequest request) {
        return String.format("""
            • Cross-selling opportunities with existing customer base
            • Technology integration to enhance %s capabilities
            • Operational efficiency through shared services and processes
            • Market expansion in %s sector
            • Combined expertise and accelerated innovation
            """,
            request.getAcquisitionPurpose() != null ? request.getAcquisitionPurpose() : "core business",
            company.getIndustry()
        );
    }

    private String generateFallbackIntegrationChallenges(CompanyProfile company) {
        return String.format("""
            • Cultural alignment and change management across organizations
            • Technology platform integration and data migration
            • Regulatory approval processes and compliance alignment
            • Customer communication and relationship continuity
            • Operational integration for %d+ employee transition
            """,
            company.getEmployeeCount() != null ? company.getEmployeeCount() : 50
        );
    }

    private String getRecommendationText(com.CHRESTAPI.todolist.enums.AcquisitionRecommendation recommendation) {
        return switch (recommendation) {
            case STRONG_BUY -> "highly attractive";
            case BUY -> "attractive";
            case HOLD -> "moderate";
            case AVOID -> "challenging";
            default -> throw new IllegalStateException("Unexpected value: " + recommendation);
        };
    }
}