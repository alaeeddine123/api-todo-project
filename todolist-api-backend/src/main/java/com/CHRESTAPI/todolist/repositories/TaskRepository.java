package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("""
        SELECT task FROM Task task 
        WHERE task.archived = false 
        AND task.shareable = true 
        AND task.assignee.id <> :userId
    """)
    Page<Task> findAllDisplayableTasks(Pageable pageable, @Param("userId") Long userId);
}