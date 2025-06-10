package com.CHRESTAPI.todolist.entities;

import com.CHRESTAPI.todolist.enums.CompanyStage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;// ====================== COMPANY PROFILE ======================
@Entity
@Table(name = "company_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "analysis_request_id")
    private AnalysisRequest analysisRequest;

    // Basic Company Info
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "website")
    private String website;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "industry")
    private String industry;

    @Column(name = "sub_industry")
    private String subIndustry;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "headquarters_location")
    private String headquartersLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_stage")
    private CompanyStage companyStage;

    // Financial Summary
    @Column(name = "last_funding_round")
    private String lastFundingRound;

    @Column(name = "total_funding", precision = 15, scale = 2)
    private BigDecimal totalFunding;

    @Column(name = "last_valuation", precision = 15, scale = 2)
    private BigDecimal lastValuation;

    @Column(name = "annual_revenue", precision = 15, scale = 2)
    private BigDecimal annualRevenue;

    @Column(name = "monthly_burn_rate", precision = 15, scale = 2)
    private BigDecimal monthlyBurnRate;

    // Technology & Product
    @Column(name = "primary_product")
    private String primaryProduct;

    @Column(name = "technology_stack", length = 1000)
    private String technologyStack;

    @Column(name = "competitive_advantages", length = 2000)
    private String competitiveAdvantages;

    // Market Position
    @Column(name = "target_market")
    private String targetMarket;

    @Column(name = "customer_base_size")
    private Integer customerBaseSize;

    @Column(name = "market_share_percentage")
    private Double marketSharePercentage;

    // Data sources and freshness
    @Column(name = "data_sources", length = 500)
    private String dataSources; // JSON string of source URLs/APIs used

    @Column(name = "data_last_updated")
    private LocalDateTime dataLastUpdated;

    // Relationships
    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RiskFactor> riskFactors;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MarketMetric> marketMetrics;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<KeyPersonnel> keyPersonnel;
}