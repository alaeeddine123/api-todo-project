package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.dto.AnalysisRequestSummaryDto;
import com.CHRESTAPI.todolist.dto.CreateAnalysisRequestDto;
import com.CHRESTAPI.todolist.dto.UserAnalysisStats;
import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.enums.RequestStatus;
import com.CHRESTAPI.todolist.repositories.AnalysisRequestRepository;
import com.CHRESTAPI.todolist.services.AiAnalysisService;
import com.CHRESTAPI.todolist.services.AnalysisRequestService;
import com.CHRESTAPI.todolist.services.CompanyDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of AnalysisRequestService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnalysisRequestServiceImpl implements AnalysisRequestService {

    private final AnalysisRequestRepository analysisRequestRepository;
    private final CompanyDataService companyDataService;
    private final AiAnalysisService aiAnalysisService;

    @Override
    public AnalysisRequest createAnalysisRequest(User user, CreateAnalysisRequestDto dto) {
        log.info("Creating analysis request for user: {} and company: {}",
                 user.getEmail(), dto.getTargetCompanyName());

        // Check if user already analyzed this company
        Optional<AnalysisRequest> existingAnalysis = analysisRequestRepository
            .findByUserAndTargetCompanyNameIgnoreCase(user, dto.getTargetCompanyName());

        if (existingAnalysis.isPresent()) {
            log.warn("User {} already analyzed company: {}", user.getEmail(), dto.getTargetCompanyName());
            throw new IllegalArgumentException("You have already analyzed this company");
        }

        // Create new analysis request
        AnalysisRequest request = new AnalysisRequest();
        request.setUser(user);
        request.setTargetCompanyName(dto.getTargetCompanyName());
        request.setTargetCompanyDomain(dto.getTargetCompanyDomain());
        request.setAcquisitionPurpose(dto.getAcquisitionPurpose());
        request.setBudgetRange(dto.getBudgetRange());
        request.setNotes(dto.getNotes());
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        AnalysisRequest savedRequest = analysisRequestRepository.save(request);

        // Trigger asynchronous analysis process
        aiAnalysisService.startAnalysisAsync(savedRequest);

        log.info("Analysis request created with ID: {}", savedRequest.getId());
        return savedRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalysisRequestSummaryDto> getUserAnalysisHistory(User user) {
        List<AnalysisRequest> requests = analysisRequestRepository.findByUserOrderByCreatedAtDesc(user);

        return requests.stream()
            .map(this::convertToSummaryDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AnalysisRequest getAnalysisRequest(Long requestId, User user) {
        AnalysisRequest request = analysisRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Analysis request not found"));

        // Verify user owns this request
        if (!request.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied to this analysis request");
        }

        return request;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalysisRequest> getRecentAnalyses(User user) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return analysisRequestRepository.findRecentAnalysesByUser(user, thirtyDaysAgo);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAnalysisStats getUserStats(User user) {
        long totalAnalyses = analysisRequestRepository.countByUser(user);
        long completedAnalyses = analysisRequestRepository.countByUserAndStatus(user, RequestStatus.COMPLETED);
        long pendingAnalyses = analysisRequestRepository.countByUserAndStatus(user, RequestStatus.PENDING) +
                              analysisRequestRepository.countByUserAndStatus(user, RequestStatus.PROCESSING);

        return UserAnalysisStats.builder()
            .totalAnalyses(totalAnalyses)
            .completedAnalyses(completedAnalyses)
            .pendingAnalyses(pendingAnalyses)
            .build();
    }

    @Override
    public void cancelAnalysisRequest(Long requestId, User user) {
        AnalysisRequest request = getAnalysisRequest(requestId, user);

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.PROCESSING) {
            throw new IllegalArgumentException("Cannot cancel completed or failed analysis");
        }

        request.setStatus(RequestStatus.CANCELLED);
        request.setCompletedAt(LocalDateTime.now());
        analysisRequestRepository.save(request);

        log.info("Analysis request {} cancelled by user {}", requestId, user.getEmail());
    }

    // Helper methods
    private AnalysisRequestSummaryDto convertToSummaryDto(AnalysisRequest request) {
        return AnalysisRequestSummaryDto.builder()
            .id(request.getId())
            .targetCompanyName(request.getTargetCompanyName())
            .status(request.getStatus())
            .createdAt(request.getCreatedAt())
            .completedAt(request.getCompletedAt())
            .budgetRange(request.getBudgetRange())
            .overallScore(request.getResult() != null ? request.getResult().getOverallScore() : null)
            .recommendation(request.getResult() != null ? request.getResult().getRecommendation().toString() : null)
            .build();
    }
}