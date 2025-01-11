package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project , Long> {
}
