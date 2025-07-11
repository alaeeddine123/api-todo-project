package com.CHRESTAPI.todolist.targets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class TargetCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;
    private String  annualRevenue;
    private String companyName;
    private String dealStatus;
    private String employeeCount;
    private String foundedYear;
    private String headquarters;
    private String industry;
    private String marketCap;
    private String priority;
    private String strategicRationale;
    private String tickerSymbol;
    private String website;
}