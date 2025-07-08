package com.CHRESTAPI.todolist.llm;

import com.CHRESTAPI.todolist.dto.CreateAnalysisRequestDto;
import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.enums.AcquisitionRecommendation;
import com.CHRESTAPI.todolist.enums.RequestStatus;
import com.CHRESTAPI.todolist.services.AnalysisRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "🧪 Testing & Development", description = "Test endpoints for validating LLM services and analysis pipeline")
public class AnalysisTestController {

    private final LlmService llmService;
    private final AnalysisRequestService analysisRequestService;

    @Operation(
        summary = "🎯 Test Analysis Scoring",
        description = "Test Claude LLM's ability to generate numerical scores (0-100) for different analysis types like strategic fit, financial health, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Score generated successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/score")
    public ResponseEntity<Map<String, Object>> testAnalysisScore(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Analysis prompt and type",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Strategic Fit Example",
                        value = """
                        {
                          "prompt": "Analyze Tesla as an acquisition target for an insurance company",
                          "analysisType": "strategic_fit"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Financial Health Example",
                        value = """
                        {
                          "prompt": "Evaluate Stripe's financial health for acquisition",
                          "analysisType": "financial_health"
                        }
                        """
                    )
                }
            )
        )
        @RequestBody Map<String, String> request) {
        log.info("Testing LLM analysis score");

        try {
            String prompt = request.getOrDefault("prompt", "Analyze Tesla as an acquisition target for an insurance company");
            String analysisType = request.getOrDefault("analysisType", "strategic_fit");

            Double score = llmService.getAnalysisScore(prompt, analysisType);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("score", score);
            response.put("prompt", prompt);
            response.put("analysisType", analysisType);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing LLM score", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "📊 Test Executive Summary Generation",
        description = "Generate a professional executive summary using Claude LLM with mock company data. Perfect for testing AI-powered report generation."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Executive summary generated successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/summary")
    public ResponseEntity<Map<String, Object>> testExecutiveSummary() {
        log.info("Testing executive summary generation");

        try {
            // Create mock data for testing
            CompanyProfile mockCompany = createMockCompanyProfile();
            com.CHRESTAPI.todolist.entities.AnalysisResult mockResult = createMockAnalysisResult();

            String summary = llmService.generateExecutiveSummary(mockCompany, mockResult);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("executiveSummary", summary);
            response.put("companyName", mockCompany.getCompanyName());
            response.put("overallScore", mockResult.getOverallScore());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing executive summary", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "💪 Test Key Strengths Analysis",
        description = "Generate detailed key strengths analysis for a mock company using Claude LLM. Tests AI's ability to identify competitive advantages."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Key strengths generated successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/strengths")
    public ResponseEntity<Map<String, Object>> testKeyStrengths() {
        log.info("Testing key strengths generation");

        try {
            CompanyProfile mockCompany = createMockCompanyProfile();
            String strengths = llmService.generateKeyStrengths(mockCompany);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("keyStrengths", strengths);
            response.put("companyName", mockCompany.getCompanyName());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing key strengths", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "⚠️ Test Risk Assessment",
        description = "Generate comprehensive risk analysis covering financial, operational, market, and regulatory risks using Claude LLM."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Risk assessment generated successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/risks")
    public ResponseEntity<Map<String, Object>> testPrimaryRisks() {
        log.info("Testing primary risks generation");

        try {
            CompanyProfile mockCompany = createMockCompanyProfile();
            String risks = llmService.generatePrimaryRisks(mockCompany);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("primaryRisks", risks);
            response.put("companyName", mockCompany.getCompanyName());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing primary risks", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "🔗 Test Synergy Identification",
        description = "Identify potential synergies and value creation opportunities from acquisition using Claude LLM's strategic analysis capabilities."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Synergies identified successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/synergies")
    public ResponseEntity<Map<String, Object>> testSynergies() {
        log.info("Testing synergies generation");

        try {
            CompanyProfile mockCompany = createMockCompanyProfile();
            AnalysisRequest mockRequest = createMockAnalysisRequest();

            String synergies = llmService.generateSynergies(mockCompany, mockRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("synergies", synergies);
            response.put("companyName", mockCompany.getCompanyName());
            response.put("acquisitionPurpose", mockRequest.getAcquisitionPurpose());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing synergies", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "🔧 Test Integration Challenges",
        description = "Assess potential integration challenges and mitigation strategies using Claude LLM's operational analysis."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Integration challenges assessed successfully"),
        @ApiResponse(responseCode = "500", description = "LLM service error")
    })
    @PostMapping("/llm/challenges")
    public ResponseEntity<Map<String, Object>> testIntegrationChallenges() {
        log.info("Testing integration challenges generation");

        try {
            CompanyProfile mockCompany = createMockCompanyProfile();
            String challenges = llmService.generateIntegrationChallenges(mockCompany);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("integrationChallenges", challenges);
            response.put("companyName", mockCompany.getCompanyName());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing integration challenges", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "🚀 Test Complete Analysis Pipeline",
        description = "Test the full end-to-end analysis workflow: company data gathering → AI analysis → risk assessment → report generation"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analysis pipeline started successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Analysis pipeline error")
    })
    @PostMapping("/analysis/complete")
    public ResponseEntity<Map<String, Object>> testCompleteAnalysis(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Analysis request details",
                content = @Content(
                    examples = @ExampleObject(
                        name = "Complete Analysis Example",
                        value = """
                        {
                          "targetCompanyName": "Stripe",
                          "targetCompanyDomain": "stripe.com",
                          "acquisitionPurpose": "Expand payment processing capabilities",
                          "budgetRange": 50000000000,
                          "notes": "Strategic acquisition for fintech expansion"
                        }
                        """
                    )
                )
            )
            @RequestBody CreateAnalysisRequestDto dto,
            Authentication authentication) {

        log.info("Testing complete analysis pipeline for: {}", dto.getTargetCompanyName());

        try {
            // Get current user (you'll need to implement this based on your auth)
            User currentUser = getCurrentUser(authentication);

            // Create analysis request
            AnalysisRequest request = analysisRequestService.createAnalysisRequest(currentUser, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("analysisRequestId", request.getId());
            response.put("targetCompany", request.getTargetCompanyName());
            response.put("status", request.getStatus());
            response.put("message", "Analysis started successfully. Check status with GET /api/analysis/" + request.getId());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing complete analysis", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(
        summary = "📈 Get Analysis Status & Results",
        description = "Check the status of an analysis request and retrieve results if completed. Useful for monitoring async analysis progress."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analysis status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Analysis request not found"),
        @ApiResponse(responseCode = "403", description = "Access denied to this analysis")
    })
    @GetMapping("/analysis/{requestId}")
    public ResponseEntity<Map<String, Object>> getAnalysisStatus(
            @Parameter(description = "Analysis request ID", example = "1")
            @PathVariable Long requestId,
            Authentication authentication) {

        try {
            User currentUser = getCurrentUser(authentication);
            AnalysisRequest request = analysisRequestService.getAnalysisRequest(requestId, currentUser);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("requestId", request.getId());
            response.put("targetCompany", request.getTargetCompanyName());
            response.put("status", request.getStatus());
            response.put("createdAt", request.getCreatedAt());
            response.put("completedAt", request.getCompletedAt());

            if (request.getResult() != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("overallScore", request.getResult().getOverallScore());
                result.put("recommendation", request.getResult().getRecommendation());
                result.put("executiveSummary", request.getResult().getExecutiveSummary());
                result.put("keyStrengths", request.getResult().getKeyStrengths());
                result.put("primaryRisks", request.getResult().getPrimaryRisks());
                response.put("result", result);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting analysis status", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Helper methods to create mock data for testing
    private CompanyProfile createMockCompanyProfile() {
        CompanyProfile company = new CompanyProfile();
        company.setCompanyName("TechCorp Solutions");
        company.setIndustry("InsurTech");
        company.setDescription("Innovative insurance technology company focused on AI-driven risk assessment and automated claims processing");
        company.setEmployeeCount(150);
        company.setAnnualRevenue(new BigDecimal("25000000"));
        company.setTotalFunding(new BigDecimal("50000000"));
        company.setFoundedYear(2019);
        company.setHeadquartersLocation("San Francisco, CA");
        company.setTechnologyStack("Python, React, AWS, PostgreSQL, Machine Learning");
        company.setCompetitiveAdvantages("Proprietary AI algorithms, strong customer relationships, innovative product suite");
        company.setTargetMarket("Small to medium insurance companies");
        company.setCustomerBaseSize(250);
        return company;
    }

    private com.CHRESTAPI.todolist.entities.AnalysisResult createMockAnalysisResult() {
        com.CHRESTAPI.todolist.entities.AnalysisResult result = new com.CHRESTAPI.todolist.entities.AnalysisResult();
        result.setOverallScore(78.5);
        result.setStrategicFitScore(82.0);
        result.setFinancialHealthScore(75.0);
        result.setMarketOpportunityScore(80.0);
        result.setIntegrationRiskScore(65.0);
        result.setRegulatoryComplianceScore(85.0);
        result.setTechnologyAlignmentScore(78.0);
        result.setRecommendation(AcquisitionRecommendation.BUY);
        return result;
    }

    private AnalysisRequest createMockAnalysisRequest() {
        AnalysisRequest request = new AnalysisRequest();
        request.setTargetCompanyName("TechCorp Solutions");
        request.setAcquisitionPurpose("Expand AI capabilities and enter InsurTech market");
        request.setBudgetRange(new BigDecimal("100000000"));
        request.setStatus(RequestStatus.PENDING);
        return request;
    }

    private User getCurrentUser(Authentication authentication) {
        // TODO: Implement based on your authentication system
        // For testing, you can create a mock user or extract from authentication
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@company.com");
        mockUser.setLastname("test");
        mockUser.setFirstname("USer");
        return mockUser;
    }
}