package com.CHRESTAPI.todolist.responses;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Integer taskId;

    private String title;

    private String description;

    private LocalDateTime due_date;

    private String status;

    private String assignee;
}
