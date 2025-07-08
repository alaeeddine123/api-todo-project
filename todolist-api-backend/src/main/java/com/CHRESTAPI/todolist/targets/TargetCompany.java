package com.CHRESTAPI.todolist.targets;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "target_companies")
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