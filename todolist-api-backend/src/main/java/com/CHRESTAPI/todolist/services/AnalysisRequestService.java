package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.dto.AnalysisRequestSummaryDto;
import com.CHRESTAPI.todolist.dto.CreateAnalysisRequestDto;
import com.CHRESTAPI.todolist.dto.UserAnalysisStats;
import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.User;

import java.util.List;

/**
 * Service interface for managing analysis requests
 */
public interface AnalysisRequestService {

    /**
     * Create a new analysis request for a user
     */
    AnalysisRequest createAnalysisRequest(User user, CreateAnalysisRequestDto dto);

    /**
     * Get user's analysis history
     */
    List<AnalysisRequestSummaryDto> getUserAnalysisHistory(User user);

    /**
     * Get analysis request by ID with user verification
     */
    AnalysisRequest getAnalysisRequest(Long requestId, User user);

    /**
     * Get user's recent analyses (last 30 days)
     */
    List<AnalysisRequest> getRecentAnalyses(User user);

    /**
     * Get user statistics
     */
    UserAnalysisStats getUserStats(User user);

    /**
     * Cancel a pending analysis request
     */
    void cancelAnalysisRequest(Long requestId, User user);

}
