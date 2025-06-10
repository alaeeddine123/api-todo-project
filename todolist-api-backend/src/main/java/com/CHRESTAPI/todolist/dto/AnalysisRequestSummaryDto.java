package com.CHRESTAPI.todolist.dto;

import com.CHRESTAPI.todolist.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequestSummaryDto {

    private Long id;
    private String targetCompanyName;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private BigDecimal budgetRange;

    // From AnalysisResult if completed
    private Double overallScore; // 0-100
    private String recommendation; // STRONG_BUY, BUY, HOLD, AVOID

    // Calculated fields
    private Long processingTimeMinutes; // How long analysis took
    private Boolean isOverdue; // If processing too long
}
