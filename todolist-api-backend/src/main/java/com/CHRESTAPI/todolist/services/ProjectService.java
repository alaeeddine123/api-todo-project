package com.CHRESTAPI.todolist.services;

import com.CHRESTAPI.todolist.entities.Project;

import java.util.List;

public interface ProjectService {

    void createProject(Project project);

    List<Project> getAllProjects();
}
