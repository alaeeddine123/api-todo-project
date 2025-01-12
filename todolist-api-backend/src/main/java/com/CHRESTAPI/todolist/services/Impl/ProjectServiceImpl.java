package com.CHRESTAPI.todolist.services.Impl;

import com.CHRESTAPI.todolist.entities.Project;
import com.CHRESTAPI.todolist.repositories.ProjectRepository;
import com.CHRESTAPI.todolist.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;


    /**
     * @param project
     */
    @Override
    public void createProject(Project project) {
        projectRepository.save(project);
    }

    /**
     * @return
     */
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
