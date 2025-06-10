import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Components
import { HomeComponent } from './home/home.component';
import { CreateProjectComponent } from './create-project/create-project.component';
import { DashBoardComponent } from './dash-board/dash-board.component';
import { MyProjectsComponent } from './my-projects/my-projects.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    //canActivate: [AuthGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashBoardComponent
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
        path: 'projects',
        component: MyProjectsComponent  // Replace with ProjectsComponent when created
      },
      {
        path: 'settings',
        component: DashBoardComponent  // Replace with SettingsComponent when created
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EspaceUserRoutingModule { }
