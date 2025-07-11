package com.CHRESTAPI.todolist.targets.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTargetCompanyDto {
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
