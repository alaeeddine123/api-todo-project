package com.CHRESTAPI.todolist.controllers;


import com.CHRESTAPI.todolist.annotations.SecuredRestController;
import com.CHRESTAPI.todolist.entities.Project;
import com.CHRESTAPI.todolist.services.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecuredRestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "project")
public class ProjectController {

    private final ProjectService projectService;


    @PostMapping("/create-project")
        public ResponseEntity<String> createProject(@RequestBody @Valid Project project){
        projectService.createProject(project);
        return ResponseEntity.ok("Project created succesfully");
    }

    @GetMapping("/get-projects-list")
        public ResponseEntity<List<Project>> getProjectsList(){
        return ResponseEntity.ok(projectService.getAllProjects());
    }

}
