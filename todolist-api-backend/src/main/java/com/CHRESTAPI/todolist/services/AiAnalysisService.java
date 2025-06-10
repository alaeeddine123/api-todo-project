package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.CompanyProfile;

/**
 * Service interface for AI-powered analysis
 */
public interface AiAnalysisService {

    /**
     * Start the complete AI analysis process asynchronously
     */
    void startAnalysisAsync(AnalysisRequest request);

    /**
     * Perform comprehensive AI analysis synchronously
     */
    AnalysisResult performAnalysis(AnalysisRequest request, CompanyProfile companyProfile);

    /**
     * Recalculate analysis with updated parameters
     */
    AnalysisResult recalculateAnalysis(AnalysisRequest request);
}