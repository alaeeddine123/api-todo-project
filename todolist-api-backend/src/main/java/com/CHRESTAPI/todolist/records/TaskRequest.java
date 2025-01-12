package com.CHRESTAPI.todolist.records;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(
        Integer taskId,

        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,

         @NotNull(message = "101")
        @NotEmpty(message = "101")
        String description,

        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String status



) {

}
