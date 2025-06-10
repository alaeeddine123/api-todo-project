package com.CHRESTAPI.todolist.entities;


        import com.CHRESTAPI.todolist.enums.RequestStatus;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.math.BigDecimal;
        import java.time.LocalDateTime;

// ====================== CORE REQUEST ENTITY ======================
@Entity
@Table(name = "analysis_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_company_name", nullable = false)
    private String targetCompanyName;

    @Column(name = "target_company_domain")
    private String targetCompanyDomain; // Optional: company website

    @Column(name = "acquisition_purpose")
    private String acquisitionPurpose; // e.g., "expand insurtech capabilities"

    @Column(name = "budget_range")
    private BigDecimal budgetRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes")
    private String notes; // User's additional context

    // Relationship to analysis result
    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnalysisResult result;

    // Relationship to discovered company data
    @OneToOne(mappedBy = "analysisRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CompanyProfile companyProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

     public String getUserEmail() {
        return user != null ? user.getEmail() : null;
    }

    public String getUserFullName() {
        return user != null ? user.getFullname() : null;
    }

    // Business logic methods
    public boolean isCompleted() {
        return status == RequestStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == RequestStatus.PENDING || status == RequestStatus.PROCESSING;
    }

    public void markAsCompleted() {
        this.status = RequestStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
