package com.CHRESTAPI.todolist.llm;

import lombok.Data;

@Data
    public  class ClaudeMessage {
        private String role;
        private String content;

        public ClaudeMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }