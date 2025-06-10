package com.CHRESTAPI.todolist.dto;

import com.CHRESTAPI.todolist.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for search and filter criteria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisSearchCriteriaDto {

    // Search terms
    private String companyName;
    private String industry;

    // Status filters
    private java.util.List<RequestStatus> statuses;

    // Score filters
    private Double minScore;
    private Double maxScore;
    private java.util.List<String> recommendations;

    // Date filters
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    // Budget filters
    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    // Sorting and pagination
    private String sortBy; // "createdAt", "score", "companyName"
    private String sortDirection; // "asc", "desc"
    private Integer page;
    private Integer size;
}