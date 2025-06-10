package com.CHRESTAPI.todolist.entities;

import com.CHRESTAPI.todolist.enums.RiskCategory;
import com.CHRESTAPI.todolist.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ====================== RISK FACTORS ======================
@Entity
@Table(name = "risk_factors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_category", nullable = false)
    private RiskCategory riskCategory;

    @Column(name = "risk_title", nullable = false)
    private String riskTitle;

    @Column(name = "risk_description", length = 1000)
    private String riskDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level", nullable = false)
    private SeverityLevel severityLevel;

    @Column(name = "impact_score") // 1-10
    private Integer impactScore;

    @Column(name = "likelihood_score") // 1-10
    private Integer likelihoodScore;

    @Column(name = "mitigation_strategies", length = 1000)
    private String mitigationStrategies;
}

