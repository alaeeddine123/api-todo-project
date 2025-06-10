package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.enums.RequestStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

 @Repository
public interface AnalysisRequestRepository extends JpaRepository<AnalysisRequest, Long> {

    // Find by user - most common queries
    List<AnalysisRequest> findByUserOrderByCreatedAtDesc(User user);

    List<AnalysisRequest> findByUserAndStatusOrderByCreatedAtDesc(User user, RequestStatus status);

    // Find user's recent analyses (last 30 days)
    @Query("SELECT ar FROM AnalysisRequest ar WHERE ar.user = :user AND ar.createdAt >= :since ORDER BY ar.createdAt DESC")
    List<AnalysisRequest> findRecentAnalysesByUser(@Param("user") User user, @Param("since") LocalDateTime since);

    // Find by company name (to check if user already analyzed this company)
    Optional<AnalysisRequest> findByUserAndTargetCompanyNameIgnoreCase(User user, String companyName);

    // Get user's analysis statistics
    @Query("SELECT COUNT(ar) FROM AnalysisRequest ar WHERE ar.user = :user")
    long countByUser(@Param("user") User user);

    @Query("SELECT COUNT(ar) FROM AnalysisRequest ar WHERE ar.user = :user AND ar.status = :status")
    long countByUserAndStatus(@Param("user") User user, @Param("status") RequestStatus status);

    // Find analyses by status (for admin/monitoring)
    List<AnalysisRequest> findByStatusOrderByCreatedAtAsc(RequestStatus status);

    // Find stuck/long-running analyses (processing for more than X hours)
    @Query("SELECT ar FROM AnalysisRequest ar WHERE ar.status = 'PROCESSING' AND ar.createdAt < :cutoffTime")
    List<AnalysisRequest> findStuckAnalyses(@Param("cutoffTime") LocalDateTime cutoffTime);

    // Find analyses created in date range
    @Query("SELECT ar FROM AnalysisRequest ar WHERE ar.user = :user AND ar.createdAt BETWEEN :startDate AND :endDate ORDER BY ar.createdAt DESC")
    List<AnalysisRequest> findByUserAndDateRange(@Param("user") User user,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
}