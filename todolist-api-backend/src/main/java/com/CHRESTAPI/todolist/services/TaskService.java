package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.entities.Task;
import com.CHRESTAPI.todolist.records.TaskRequest;
import com.CHRESTAPI.todolist.responses.PageResponse;
import com.CHRESTAPI.todolist.responses.TaskResponse;
import org.springframework.security.core.Authentication;

public interface TaskService {
    Integer save(TaskRequest taskRequest , Authentication connectedUser);

    TaskResponse findTaskByid(Long taskId);

    PageResponse<TaskResponse> findAllByTaks(int page, int size, Authentication connectedUser);

    PageResponse<TaskResponse>  findAllByTaksByAssignee(int page, int size, Authentication connectedUser);

    PageResponse<TaskResponse> findAllTasksByStatus ( int page, int size, Authentication connectedUser , String status);

    Long updateShareableStatus(Long taskId, Authentication connectedUser);
}
