package com.CHRESTAPI.
        todolist.mappers;


import com.CHRESTAPI.todolist.entities.Task;
import com.CHRESTAPI.todolist.records.TaskRequest;
import com.CHRESTAPI.todolist.responses.TaskResponse;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
    public Task toTask(TaskRequest taskRequest) {
        return Task.builder()
                .taskId(taskRequest.taskId())
                .title(taskRequest.title())
                .build();
    }

    public TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .due_date(task.getDue_date())
                .status(task.getStatus())
                .assignee(task.getAssignee() != null ? task.getAssignee().getFullname() : null)
                .build();
    }
}