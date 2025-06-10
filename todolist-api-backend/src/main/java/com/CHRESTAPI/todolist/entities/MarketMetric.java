package com.CHRESTAPI.todolist.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ====================== MARKET METRICS ======================
@Entity
@Table(name = "market_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    @Column(name = "metric_name", nullable = false)
    private String metricName; // e.g., "Total Addressable Market", "Growth Rate"

    @Column(name = "metric_value", precision = 15, scale = 2)
    private BigDecimal metricValue;

    @Column(name = "metric_unit")
    private String metricUnit; // e.g., "USD", "Percentage", "Count"

    @Column(name = "time_period")
    private String timePeriod; // e.g., "2024", "Q1 2024", "YoY"

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}

