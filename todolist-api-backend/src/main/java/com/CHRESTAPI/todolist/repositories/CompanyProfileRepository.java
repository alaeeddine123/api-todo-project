package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.AnalysisRequest;
import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.enums.CompanyStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    // Find by analysis request
    Optional<CompanyProfile> findByAnalysisRequest(AnalysisRequest analysisRequest);

    // Search companies by name (for potential duplicate detection)
    List<CompanyProfile> findByCompanyNameContainingIgnoreCase(String companyName);

    // Find by industry
    List<CompanyProfile> findByIndustryIgnoreCase(String industry);

    // Find by company stage
    List<CompanyProfile> findByCompanyStage(CompanyStage stage);

    // Find companies in funding range
    @Query("SELECT cp FROM CompanyProfile cp WHERE cp.totalFunding BETWEEN :minFunding AND :maxFunding")
    List<CompanyProfile> findByFundingRange(@Param("minFunding") java.math.BigDecimal minFunding,
                                            @Param("maxFunding") java.math.BigDecimal maxFunding);

    // Find companies by employee count range
    @Query("SELECT cp FROM CompanyProfile cp WHERE cp.employeeCount BETWEEN :minEmployees AND :maxEmployees")
    List<CompanyProfile> findByEmployeeCountRange(@Param("minEmployees") Integer minEmployees,
                                                  @Param("maxEmployees") Integer maxEmployees);

    // Find companies with data updated recently
    @Query("SELECT cp FROM CompanyProfile cp WHERE cp.dataLastUpdated >= :since")
    List<CompanyProfile> findWithRecentData(@Param("since") LocalDateTime since);

    // Find companies needing data refresh (data older than X days)
    @Query("SELECT cp FROM CompanyProfile cp WHERE cp.dataLastUpdated < :cutoffDate OR cp.dataLastUpdated IS NULL")
    List<CompanyProfile> findNeedingDataRefresh(@Param("cutoffDate") LocalDateTime cutoffDate);
}
