package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.KeyPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyPersonnelRepository extends JpaRepository<KeyPersonnel, Long> {

    // Find personnel by company
    List<KeyPersonnel> findByCompanyProfile(CompanyProfile companyProfile);

    // Find by position (CEO, CTO, etc.)
    List<KeyPersonnel> findByPositionContainingIgnoreCase(String position);

    // Find high retention risks
    @Query("SELECT kp FROM KeyPersonnel kp WHERE kp.retentionRiskScore >= :riskThreshold ORDER BY kp.retentionRiskScore DESC")
    List<KeyPersonnel> findHighRetentionRisks(@Param("riskThreshold") Integer riskThreshold);

    // Find experienced personnel (years with company)
    @Query("SELECT kp FROM KeyPersonnel kp WHERE kp.yearsWithCompany >= :minYears")
    List<KeyPersonnel> findExperiencedPersonnel(@Param("minYears") Integer minYears);

    // Find key positions for a company
    @Query("SELECT kp FROM KeyPersonnel kp WHERE kp.companyProfile = :company AND kp.position IN ('CEO', 'CTO', 'CFO', 'Founder') ORDER BY kp.position")
    List<KeyPersonnel> findExecutiveTeam(@Param("company") CompanyProfile company);

    // Count personnel by company
    @Query("SELECT COUNT(kp) FROM KeyPersonnel kp WHERE kp.companyProfile = :company")
    long countByCompanyProfile(@Param("company") CompanyProfile company);

    // Find personnel with specific background/experience
    @Query("SELECT kp FROM KeyPersonnel kp WHERE kp.background LIKE CONCAT('%', :keyword, '%') OR kp.previousExperience LIKE CONCAT('%', :keyword, '%')")
    List<KeyPersonnel> findByExperienceKeyword(@Param("keyword") String keyword);
}