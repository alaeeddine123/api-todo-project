package com.CHRESTAPI.todolist.llm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
    public  class ClaudeRequest {
        private String model;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        private List<ClaudeMessage> messages;
    }