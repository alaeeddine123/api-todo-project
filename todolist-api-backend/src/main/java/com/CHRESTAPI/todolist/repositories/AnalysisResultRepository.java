package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.AnalysisResult;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.enums.AcquisitionRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    // Find by analysis request
    Optional<AnalysisResult> findByRequest(AnalysisRequest request);

    // Find by recommendation type
    List<AnalysisResult> findByRecommendation(AcquisitionRecommendation recommendation);

    // Find high-scoring analyses (score above threshold)
    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.overallScore >= :minScore ORDER BY ar.overallScore DESC")
    List<AnalysisResult> findHighScoringAnalyses(@Param("minScore") Double minScore);

    // Find analyses by score range
    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.overallScore BETWEEN :minScore AND :maxScore ORDER BY ar.overallScore DESC")
    List<AnalysisResult> findByScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);

    // Get user's best recommendations
    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.request.user = :user AND ar.recommendation IN ('STRONG_BUY', 'BUY') ORDER BY ar.overallScore DESC")
    List<AnalysisResult> findUsersBestRecommendations(@Param("user") User user);

    // Find analyses with high confidence
    @Query("SELECT ar FROM AnalysisResult ar WHERE ar.aiConfidenceScore >= :minConfidence ORDER BY ar.aiConfidenceScore DESC")
    List<AnalysisResult> findHighConfidenceAnalyses(@Param("minConfidence") Double minConfidence);

    // Performance analytics - average processing time
    @Query("SELECT AVG(ar.processingTimeSeconds) FROM AnalysisResult ar WHERE ar.createdAt >= :since")
    Double getAverageProcessingTime(@Param("since") LocalDateTime since);

    // Get analysis result statistics for a user
    @Query("SELECT " +
           "COUNT(ar) as totalAnalyses, " +
           "AVG(ar.overallScore) as avgScore, " +
           "MAX(ar.overallScore) as maxScore, " +
           "MIN(ar.overallScore) as minScore " +
           "FROM AnalysisResult ar WHERE ar.request.user = :user")
    Object[] getUserAnalyticsStats(@Param("user") User user);
}
