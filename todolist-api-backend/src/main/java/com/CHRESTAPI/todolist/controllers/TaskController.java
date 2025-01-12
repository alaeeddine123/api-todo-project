package com.CHRESTAPI.todolist.controllers;

import com.CHRESTAPI.todolist.records.TaskRequest;
import com.CHRESTAPI.todolist.responses.PageResponse;
import com.CHRESTAPI.todolist.responses.TaskResponse;
import com.CHRESTAPI.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
@Tag(name = "Task")
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<Integer> saveTask(
            @Valid @RequestBody TaskRequest taskRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(taskService.save(taskRequest, connectedUser));
    }

    @GetMapping("{task-id}")
    public ResponseEntity<TaskResponse> findTaskById(
            @PathVariable("task-id") Long taskId
    ) {
        return ResponseEntity.ok(taskService.findTaskByid(taskId));
    }

    @GetMapping()
    public ResponseEntity <PageResponse<TaskResponse>> findAllTasks(
            @RequestParam ( name = "page" , defaultValue = "0", required = false) int page,
            @RequestParam ( name = "size" , defaultValue = "10", required = false) int size,
            Authentication  connectedUser
            ){
    return ResponseEntity.ok(taskService.findAllByTaks(page,size,connectedUser));
    }


    @GetMapping("/assignee")
        public ResponseEntity <PageResponse<TaskResponse>> findAllTaksByAssignee(
            @RequestParam ( name = "page" , defaultValue = "0", required = false) int page,
            @RequestParam ( name = "size" , defaultValue = "10", required = false) int size,
            Authentication  connectedUser
            ){
    return ResponseEntity.ok(taskService.findAllByTaksByAssignee(page,size,connectedUser));
    }


    @GetMapping("status")
    public ResponseEntity<PageResponse<TaskResponse>> findAllTasksByStatus(
            @RequestParam ( name = "page" , defaultValue = "0", required = false) int page,
            @RequestParam ( name = "size" , defaultValue = "10", required = false) int size,
            @PathVariable("task-id") String status,
            Authentication  connectedUser
    ){
        return ResponseEntity.ok(taskService.findAllTasksByStatus(page, size, connectedUser, status));
    }

    @PatchMapping("/shareable/{task-id}")
    public ResponseEntity<Long> updateShareableStatus(
            @PathVariable("task-id") Long taskId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(taskService.updateShareableStatus(taskId ,  connectedUser));
    }


}


