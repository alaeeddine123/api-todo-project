package com.CHRESTAPI.todolist.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ====================== KEY PERSONNEL ======================
@Entity
@Table(name = "key_personnel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "background", length = 1000)
    private String background;

    @Column(name = "years_with_company")
    private Integer yearsWithCompany;

    @Column(name = "previous_experience", length = 1000)
    private String previousExperience;

    @Column(name = "retention_risk_score") // 1-10 (higher = more likely to leave)
    private Integer retentionRiskScore;
}