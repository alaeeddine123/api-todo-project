package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.MarketMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MarketMetricRepository extends JpaRepository<MarketMetric, Long> {

    // Find metrics by company profile
    List<MarketMetric> findByCompanyProfile(CompanyProfile companyProfile);

    // Find specific metric by name
    List<MarketMetric> findByCompanyProfileAndMetricNameIgnoreCase(CompanyProfile companyProfile, String metricName);

    // Find metrics by time period
    List<MarketMetric> findByTimePeriod(String timePeriod);

    // Find latest metrics (most recently updated)
    @Query("SELECT mm FROM MarketMetric mm WHERE mm.lastUpdated >= :since ORDER BY mm.lastUpdated DESC")
    List<MarketMetric> findRecentMetrics(@Param("since") LocalDateTime since);

    // Find metrics needing update
    @Query("SELECT mm FROM MarketMetric mm WHERE mm.lastUpdated < :cutoffDate OR mm.lastUpdated IS NULL")
    List<MarketMetric> findStaleMetrics(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Get all available metric names (for UI dropdowns)
    @Query("SELECT DISTINCT mm.metricName FROM MarketMetric mm ORDER BY mm.metricName")
    List<String> findDistinctMetricNames();

    // Find companies with specific metric above threshold
    @Query("SELECT mm.companyProfile FROM MarketMetric mm WHERE mm.metricName = :metricName AND mm.metricValue >= :threshold")
    List<CompanyProfile> findCompaniesWithMetricAbove(@Param("metricName") String metricName, @Param("threshold") java.math.BigDecimal threshold);
}
