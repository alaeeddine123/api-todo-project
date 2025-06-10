package com.CHRESTAPI.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for successful API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private java.util.Map<String, Object> metadata; // pagination, etc.
}