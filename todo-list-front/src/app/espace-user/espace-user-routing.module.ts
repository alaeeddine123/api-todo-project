import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Components
import { CreateProjectComponent } from './create-project/create-project.component';
import { DashBoardComponent } from './dash-board/dash-board.component';
//import { MyProjectsComponent } from './my-projects/my-projects.component';

const routes: Routes = [
  {
    path: '',
    component: DashBoardComponent  // Direct route to dashboard
  },
  {
    path: 'new-project',
    component: CreateProjectComponent
  },
  {
    path: 'tasks',
    component: DashBoardComponent  // Replace with TasksComponent when created
  },
  {
    path: 'settings',
    component: DashBoardComponent  // Replace with SettingsComponent when created
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EspaceUserRoutingModule { }
