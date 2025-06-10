package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for API error responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private java.util.List<String> details; // Field validation errors
}