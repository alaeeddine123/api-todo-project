package com.CHRESTAPI.todolist.llm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
     public  class ClaudeResponse {
        private String id;
        private String type;
        private String role;
        private List<ClaudeContent> content;
    }