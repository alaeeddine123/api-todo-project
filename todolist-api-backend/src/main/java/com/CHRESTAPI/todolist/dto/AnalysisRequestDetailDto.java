package com.CHRESTAPI.todolist.dto;

import com.CHRESTAPI.todolist.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for detailed analysis request view
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequestDetailDto {

    private Long id;
    private String targetCompanyName;
    private String targetCompanyDomain;
    private String acquisitionPurpose;
    private BigDecimal budgetRange;
    private String notes;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // User information
    private String userEmail;
    private String userFullName;

    // Company profile (if data gathered)
    private CompanyProfileSummaryDto companyProfile;

    // Analysis result (if completed)
    private AnalysisResultSummaryDto analysisResult;

    // Progress information
    private String statusMessage;
    private Integer progressPercentage; // 0-100
}

