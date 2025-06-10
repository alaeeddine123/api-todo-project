package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.CompanyProfile;
import com.CHRESTAPI.todolist.entities.RiskFactor;
import com.CHRESTAPI.todolist.enums.RiskCategory;
import com.CHRESTAPI.todolist.enums.SeverityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskFactorRepository extends JpaRepository<RiskFactor, Long> {

    // Find risks by company profile
    List<RiskFactor> findByCompanyProfile(CompanyProfile companyProfile);

    // Find risks by category
    List<RiskFactor> findByRiskCategory(RiskCategory category);

    // Find high-severity risks
    List<RiskFactor> findBySeverityLevel(SeverityLevel severityLevel);

    // Find risks by impact score range
    @Query("SELECT rf FROM RiskFactor rf WHERE rf.impactScore >= :minImpact ORDER BY rf.impactScore DESC")
    List<RiskFactor> findHighImpactRisks(@Param("minImpact") Integer minImpact);

    // Find critical risks (high impact + high likelihood)
    @Query("SELECT rf FROM RiskFactor rf WHERE rf.impactScore >= :minImpact AND rf.likelihoodScore >= :minLikelihood")
    List<RiskFactor> findCriticalRisks(@Param("minImpact") Integer minImpact, @Param("minLikelihood") Integer minLikelihood);

    // Count risks by category for a company
    @Query("SELECT rf.riskCategory, COUNT(rf) FROM RiskFactor rf WHERE rf.companyProfile = :company GROUP BY rf.riskCategory")
    List<Object[]> countRisksByCategory(@Param("company") CompanyProfile company);

    // Find companies with the most risks in a category
    @Query("SELECT rf.companyProfile, COUNT(rf) as riskCount FROM RiskFactor rf WHERE rf.riskCategory = :category GROUP BY rf.companyProfile ORDER BY riskCount DESC")
    List<Object[]> findCompaniesWithMostRisks(@Param("category") RiskCategory category);
}
