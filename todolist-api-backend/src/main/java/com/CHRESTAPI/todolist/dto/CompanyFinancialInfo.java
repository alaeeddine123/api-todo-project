package com.CHRESTAPI.todolist.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanyFinancialInfo {
    private java.math.BigDecimal totalFunding;
    private java.math.BigDecimal valuation;
    private java.math.BigDecimal revenue;
    private String lastFundingRound;
}
