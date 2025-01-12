package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.entities.Task;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.exception.OperationNotPermittedException;
import com.CHRESTAPI.todolist.mappers.TaskMapper;
import com.CHRESTAPI.todolist.records.TaskRequest;
import com.CHRESTAPI.todolist.repositories.TaskRepository;
import com.CHRESTAPI.todolist.responses.PageResponse;
import com.CHRESTAPI.todolist.responses.TaskResponse;
import com.CHRESTAPI.todolist.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.CHRESTAPI.todolist.models.TaskSpecification.withAssigneeId;
import static com.CHRESTAPI.todolist.models.TaskSpecification.withStatus;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Integer save(TaskRequest taskRequest, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Task task = taskMapper.toTask(taskRequest);
        task.setAssignee(user);
        return taskRepository.save(task).getTaskId();
    }

    public TaskResponse findTaskByid(Long taskId) {
        return taskRepository.findById(taskId)
                .map(taskMapper::toTaskResponse)  //
                .orElseThrow(() -> new EntityNotFoundException("task not found"));
    }

    @Override
    public PageResponse<TaskResponse> findAllByTaks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
        Page<Task> tasks = taskRepository.findAllDisplayableTasks(pageable, user.getId());
        List<TaskResponse> taskResponses = tasks.stream().map(
                taskMapper::toTaskResponse
        ).toList();
        return new PageResponse<>(
                taskResponses,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isFirst(),
                tasks.isLast()
        );
    }

    @Override
    public PageResponse<TaskResponse> findAllByTaksByAssignee(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
        Page<Task> tasks = taskRepository.findAll(withAssigneeId(user.getId()), pageable);

        List<TaskResponse> taskResponses = tasks.stream().map(
                taskMapper::toTaskResponse
        ).toList();
        return new PageResponse<>(
                taskResponses,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isFirst(),
                tasks.isLast()
        );
    }

    @Override
    public PageResponse<TaskResponse> findAllTasksByStatus(int page, int size, Authentication connectedUser, String status) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
        Page<Task> tasks = taskRepository.findAll(withStatus(status), pageable);


        List<TaskResponse> taskResponses = tasks.stream().map(
                taskMapper::toTaskResponse
        ).toList();
        return new PageResponse<>(
                taskResponses,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isFirst(),
                tasks.isLast()
        );
    }

    @Override
    public Long updateShareableStatus(Long taskId, Authentication connectedUser) {
        Task task = taskRepository.findById(taskId).orElseThrow(()->
                new EntityNotFoundException("not task found with ID ->"+taskId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(task.getAssignee().getTasks(),user.getId())){
           throw new OperationNotPermittedException("you are not not allowed to update status");
        }
        task.setShareable(!task.isShareable());
        taskRepository.save(task);
        return taskId;
    }


}
