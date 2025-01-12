import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../models/project-interface';
import { ProjectService } from '../project.service';

@Component({
  selector: 'app-my-projects',
  templateUrl: './my-projects.component.html',
  styleUrl: './my-projects.component.scss'
})
export class MyProjectsComponent  implements OnInit{



  projects: Project[] = [];
  loading = true;
  error: string | null = null;
  /*  {
      id: 1,
      title: 'Project A',
      description: 'Description of Project A',
      dueDate: new Date('2025-02-15'),
      priority: 'High',
      teamSize: 'Medium (4-7)'
    },
    {
      id: 2,
      title: 'Project B',
      description: 'Description of Project B',
      dueDate: new Date('2025-03-10'),
      priority: 'Medium',
      teamSize: 'Small (1-3)'
    },
    {
      id: 3,
      title: 'Project C',
      description: 'Description of Project C',
      dueDate: new Date('2025-04-20'),
      priority: 'Low',
      teamSize: 'Large (8+)'
    }
  ];*/

  constructor(private projectService:ProjectService) {}

  ngOnInit(): void {
    this.getProjectsList();
  }

  getProjectsList(){
    this.projectService.getAllProjects().subscribe({
      next:(data) => {
        this.projects = data;
        this.loading = false;
      },
      error :(error) => {
        console.error(" error fetching project list ",error);
        this.loading = false;
      }
    })
  }
}

