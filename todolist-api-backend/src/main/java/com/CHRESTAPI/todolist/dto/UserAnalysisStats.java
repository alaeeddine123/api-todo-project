package com.CHRESTAPI.todolist.dto;

@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class UserAnalysisStats {
    private long totalAnalyses;
    private long completedAnalyses;
    private long pendingAnalyses;
}