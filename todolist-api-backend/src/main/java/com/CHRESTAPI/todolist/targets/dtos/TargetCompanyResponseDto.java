package com.CHRESTAPI.todolist.targets.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TargetCompanyResponseDto {
    private Long targetId;
    private String companyName;
    private String tickerSymbol;
    private String website;
    private String marketCap;
    private String annualRevenue;
    private String industry;
    private String priority;
    private String dealStatus;
    private String strategicRationale;
    private String headquarters;
    private String employeeCount;
    private String foundedYear;
}